package goorming.iCurriculum.take.service;

import goorming.iCurriculum.common.code.status.ErrorStatus;
import goorming.iCurriculum.common.exception.GeneralException;
import goorming.iCurriculum.course.Category;
import goorming.iCurriculum.course.Course;
import goorming.iCurriculum.course.CourseRepository;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import goorming.iCurriculum.essentialcourse.EssentialCourseRepository;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.MemberRepository;
import goorming.iCurriculum.take.entity.Take;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO.DashboardDTO;
import goorming.iCurriculum.take.exception.TakeException;
import goorming.iCurriculum.take.repository.TakeRepository;
import goorming.iCurriculum.take.entity.dto.TakeRequestDTO;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO;
import goorming.iCurriculum.take.service.util.CourseFilter;
import goorming.iCurriculum.take.service.util.TakeConverter;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TakeService {
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final EssentialCourseRepository essentialCourseRepository;
    private final TakeRepository takeRepository;

    public TakeResponseDTO.TakenCourseListDTO findTakeList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Take> takeList = member.getTakeList();
        return TakeConverter.convertTakeList(takeList);
    }

    @Transactional
    public void takeCourse(Long memberId, TakeRequestDTO.CreateTakeListDTO createTakeListDTO) {
        // memberid, courseid 검증로직 추가
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Long> courseIdList = createTakeListDTO.getTakeCourseDTOList().stream()
                .map(TakeRequestDTO.CreateTakeDTO::getCourseId)
                .collect(Collectors.toList());

        List<Course> courseList = courseRepository.findCoursesByIds(courseIdList);

        if (courseList.size() != courseIdList.size()) {
            throw new TakeException(ErrorStatus.COURSE_NOT_FOUND);
        }

        createTakeListDTO.getTakeCourseDTOList().stream()
                .flatMap(createTakeDTO -> courseList.stream()
                        .filter(course -> course.getId().equals(createTakeDTO.getCourseId()))
                        .map(course -> TakeConverter.toTake(createTakeDTO, course, member))
                )
                .forEach(takeRepository::save);
    }

    @Transactional
    public TakeResponseDTO.TakenCourseDTO updateTake(Long takeId,
                                                     TakeRequestDTO.UpdateTakenCourseDTO updateTakenCourseDTO) {
        Take take = takeRepository.findById(takeId).orElseThrow(
                () -> new TakeException(ErrorStatus.TAKE_NOT_FOUND));
        take.update(updateTakenCourseDTO.getTakenTerm(), updateTakenCourseDTO.getGrade());

        return TakeConverter.convertTake(take);
    }

    public TakeResponseDTO.UntakenCourseListDTO findUntakenList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Long> takenCourseIdList = member.getTakeList().stream()
                .map(take -> take.getCourse().getId())
                .collect(Collectors.toList());
        List<Course> untakenList = courseRepository.findCoursesNotInIds(takenCourseIdList);
        return TakeConverter.convertUntakeList(makePersonalizedUntakenCourseList(untakenList, member));
    }

    @Transactional
    public void deleteTake(Long takeId) {
        Take take = takeRepository.findById(takeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TAKE_NOT_FOUND));
        takeRepository.deleteById(takeId);
    }

    @Transactional
    public DashboardDTO findMemberStat(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Take> takeList = member.getTakeList();

        Map<Category, Integer> creditByCategory = calculateCreditByCategory(takeList);

        List<Course> untakenCourses = courseRepository.findCoursesNotInIds(takeList.stream()
                .map(take -> take.getCourse().getId())
                .collect(Collectors.toList()));

        List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList
                = makePersonalizedUntakenCourseList(untakenCourses, member);

        TakeResponseDTO.TakenCategoryDTO majorEssentialDTO = TakeConverter.convertPersonalCategoryCourseList(
                creditByCategory.get(Category.MAJOR_ESSENTIAL),
                filterTop5ByCategory(untakenCourseDTOList, Category.MAJOR_ESSENTIAL));
        TakeResponseDTO.TakenCategoryDTO majorSelectiveDTO = TakeConverter.convertPersonalCategoryCourseList(
                creditByCategory.get(Category.MAJOR_SELECTIVE),
                filterTop5ByCategory(untakenCourseDTOList, Category.MAJOR_SELECTIVE));
        TakeResponseDTO.TakenCategoryDTO generalEssentialDTO = TakeConverter.convertPersonalCategoryCourseList(
                creditByCategory.get(Category.GENERAL_ESSENTIAL),
                filterTop5ByCategory(untakenCourseDTOList, Category.GENERAL_ESSENTIAL));
        TakeResponseDTO.TakenCategoryDTO generalSelectiveDTO = TakeConverter.convertPersonalCategoryCourseList(
                creditByCategory.get(Category.GENERAL_SELECTIVE),
                filterTop5ByCategory(untakenCourseDTOList, Category.GENERAL_SELECTIVE));
        TakeResponseDTO.GeneralCoreDTO generalCoreDTO = makePersonalGeneralCoreDTO(creditByCategory, untakenCourseDTOList);

        return TakeConverter.convertToMemberStats(
                takeList,
                majorEssentialDTO,
                majorSelectiveDTO,
                generalEssentialDTO,
                generalSelectiveDTO,
                generalCoreDTO
        );

    }


    private Map<Category, Integer> calculateCreditByCategory(List<Take> takeList) {
        // 카테고리별로 Take 객체들을 그룹화하고, 이수 학점을 합산한다.
        Map<Category, Integer> creditByCategory = takeList.stream()
                .collect(Collectors.groupingBy(
                        take -> take.getCourse().getCategory(),
                        Collectors.summingInt(take -> take.getCourse().getCredit())
                ));

        // 모든 카테고리에 대해 이수 학점이 없는 경우 0으로 설정한다.
        for (Category category : Category.values()) {
            creditByCategory.putIfAbsent(category, 0);
        }

        return creditByCategory;

    }

    private TakeResponseDTO.GeneralCoreDTO makePersonalGeneralCoreDTO(
            Map<Category, Integer> creditByCategory,
            List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList) {
        List<Category> generalCoreCategories = Arrays.asList(
                Category.GENERAL_CORE_ONE,
                Category.GENERAL_CORE_TWO,
                Category.GENERAL_CORE_THREE,
                Category.GENERAL_CORE_FOUR,
                Category.GENERAL_CORE_FIVE,
                Category.GENERAL_CORE_SIX,
                Category.GENERAL_CREATIVE
        );

        return TakeConverter.convertPersonalGeneralCoreCourse(
                TakeConverter.convertPersonalCategoryCourseList(generalCoreCategories.stream()
                                .map(creditByCategory::get) // 각 Category에 대한 학점을 가져옴
                                .mapToInt(Integer::intValue) // Integer를 int로 변환
                                .sum(),
                        filterTop5ByCategories(untakenCourseDTOList, generalCoreCategories)),
                generalCoreCategories.stream()
                        .map(creditByCategory::get) // 각 Category에 대한 학점을 가져옴
                        .collect(Collectors.toList()));
    }

    private List<TakeResponseDTO.UntakenCourseDTO> makePersonalizedUntakenCourseList(
            List<Course> untakenCourseList, Member member) {
        List<EssentialCourse> essentialCourseList = essentialCourseRepository.findByDepartmentId(
                member.getDepartment().getId());
        return untakenCourseList.stream()
                .map(course -> {
                    Category category = CourseFilter.filterCourse(member, course, essentialCourseList);
                    return TakeConverter.convertUntake(course, category);
                })
                .collect(Collectors.toList());
    }

    private List<TakeResponseDTO.UntakenCourseDTO> filterTop5ByCategory(
            List<TakeResponseDTO.UntakenCourseDTO> courses, Category category) {
        return courses.stream()
                .filter(course -> course.getCategoryName().equals(category.getName()))
                .sorted(Comparator.comparingInt(course -> -course.getTakenNumber())) // 수강 인원 내림차순 정렬
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<TakeResponseDTO.UntakenCourseDTO> filterTop5ByCategories(
            List<TakeResponseDTO.UntakenCourseDTO> courses, List<Category> categories) {
        return courses.stream()
                .filter(course -> categories.stream()
                        .map(Category::getName).toList()
                        .contains(course.getCategoryName())) // Category 이름과 Course의 Category 이름을 비교
                .sorted(Comparator.comparingInt(course -> -course.getTakenNumber())) // 수강 인원 내림차순 정렬
                .limit(5)
                .collect(Collectors.toList());
    }

}
