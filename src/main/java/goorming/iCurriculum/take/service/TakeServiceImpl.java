package goorming.iCurriculum.take.service;

import goorming.iCurriculum.common.code.status.ErrorStatus;
import goorming.iCurriculum.common.exception.GeneralException;
import goorming.iCurriculum.course.entity.Category;
import goorming.iCurriculum.course.entity.Course;
import goorming.iCurriculum.course.repository.CourseRepository;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import goorming.iCurriculum.essentialcourse.EssentialCourseRepository;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.MemberRepository;
import goorming.iCurriculum.take.entity.Grade;
import goorming.iCurriculum.take.entity.Take;
import goorming.iCurriculum.take.exception.TakeException;
import goorming.iCurriculum.take.repository.TakeRepository;
import goorming.iCurriculum.take.entity.dto.TakeRequestDTO;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO;
import goorming.iCurriculum.take.service.util.CourseFilter;
import goorming.iCurriculum.take.service.util.TakeConverter;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TakeServiceImpl implements TakeService {
    private final MemberRepository memberRepository;
    private final CourseRepository courseRepository;
    private final EssentialCourseRepository essentialCourseRepository;
    private final TakeRepository takeRepository;

    // 멤버의 수강 리스트를 조회하는 메서드
    @Override
    @Transactional
    public TakeResponseDTO.TakenCourseListDTO findTakeList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Take> takeList = member.getTakeList();
        return TakeConverter.convertTakeList(takeList);
    }

    @Override

    @Transactional
    // 새로운 과목을 수강하는 메서드
    public List<Take> takeCourse(Long memberId, TakeRequestDTO.CreateTakeListDTO createTakeListDTO) {
        // memberId 검증 로직
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 수강하려는 과목들의 ID 리스트
        List<Long> courseIdList = createTakeListDTO.getCreateTakeDTOList()
                .stream()
                .map(TakeRequestDTO.CreateTakeDTO::getCourseId)
                .collect(Collectors.toList());

        // 이미 수강한 과목들의 과목 ID 리스트
        List<Long> takenCourseIdList = member.getTakeList().stream()
                .map(take -> take.getCourse().getId()).toList();

        // 교집합 구하기
        Set<Long> courseIdSet = new HashSet<>(courseIdList);
        courseIdSet.retainAll(takenCourseIdList);

        if (!courseIdSet.isEmpty()) {
            throw new TakeException(ErrorStatus.TAKE_DUPLICATED);
        }

        // courseIdList에 해당하는 과목 리스트를 데이터베이스에서 조회
        List<Course> courseList = courseRepository.findCoursesByIds(courseIdList);

        // 조회된 과목 수와 요청된 과목 수가 다를 경우 예외 발생
        if (courseList.size() != courseIdList.size()) {
            throw new TakeException(ErrorStatus.COURSE_NOT_FOUND);
        }

        List<Take> takeList = new ArrayList<>();
        // 각 createTakeDTO와 course를 매핑하고, Take 객체를 생성 및 저장
        createTakeListDTO.getCreateTakeDTOList().stream()
                .flatMap(createTakeDTO -> courseList.stream()
                        .filter(course -> course.getId().equals(createTakeDTO.getCourseId()))
                        .map(course -> {
                            Take take = TakeConverter.toTake(createTakeDTO, course, member);
                            course.takeThisCourse();// 수강 인원 증가
                            takeList.add(take);
                            return take;
                        })
                )
                .forEach(takeRepository::save);
        return takeList;
    }

    @Override

    @Transactional
    // 수강 과목 정보를 업데이트하는 메서드
    public TakeResponseDTO.TakenCourseDTO updateTake(Long takeId,
                                                     TakeRequestDTO.UpdateTakenCourseDTO updateTakenCourseDTO) {
        Take take = takeRepository.findById(takeId).orElseThrow(
                () -> new TakeException(ErrorStatus.TAKE_NOT_FOUND));

        // 수강 과목 정보 업데이트
        take.update(updateTakenCourseDTO.getTakenTerm(), Grade.getGrade(updateTakenCourseDTO.getGrade()),
                Category.getCategoryByName(
                        updateTakenCourseDTO.getCategory()));

        return TakeConverter.convertTake(take);
    }

    @Override

    @Transactional
    // 미이수 과목 리스트를 조회하는 메서드
    public TakeResponseDTO.UntakenCourseListDTO findUntakenList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 이수한 과목 ID 리스트 생성
        List<Long> takenCourseIdList = member.getTakeList().stream()
                .map(take -> take.getCourse().getId())
                .collect(Collectors.toList());

        // 미이수 과목 리스트 조회
        // 이수 과목 제외 하고 모든 과목 리스트 조회
        List<Course> untakenList = courseRepository.findCoursesNotInIds(takenCourseIdList);

        return TakeConverter.convertUntakeList(makePersonalizedUntakenCourseList(untakenList, member));
    }

    @Override

    @Transactional
    // 수강 과목을 삭제하는 메서드
    public void deleteTake(Long takeId) {
        Take take = takeRepository.findById(takeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TAKE_NOT_FOUND));

        // 과목 수강인원수 감소
        take.getCourse().dropThisCourse();

        takeRepository.deleteById(takeId);
    }

    @Override

    @Transactional
    // 멤버의 통계를 조회하는 메서드
    public TakeResponseDTO.DashboardDTO findMemberStat(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Take> takeList = member.getTakeList();

        // 카테고리별 이수 학점 계산
        Map<Category, Integer> creditByCategory = calculateCreditByCategory(takeList);

        // 미이수 과목 리스트 조회
        List<Course> untakenCourses = courseRepository.findCoursesNotInIds(takeList.stream()
                .map(take -> take.getCourse().getId())
                .collect(Collectors.toList()));

        // 미이수 과목 DTO 리스트 생성
        List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList
                = makePersonalizedUntakenCourseList(untakenCourses, member);

        // 카테고리별 미이수 과목 리스트 생성
        TakeResponseDTO.TakenCategoryDTO takenMajorDTO = TakeConverter.convertPersonalCategoryCourseList(
                creditByCategory.get(Category.MAJOR_ESSENTIAL), creditByCategory.get(Category.MAJOR_SELECTIVE),
                filterTop5ByCategories(untakenCourseDTOList, Arrays.asList(
                        Category.MAJOR_ESSENTIAL,
                        Category.MAJOR_SELECTIVE
                )));
        TakeResponseDTO.TakenCategoryDTO takenGeneralDTO = TakeConverter.convertPersonalCategoryCourseList(
                creditByCategory.get(Category.GENERAL_ESSENTIAL), creditByCategory.get(Category.GENERAL_SELECTIVE),
                filterTop5ByCategories(untakenCourseDTOList, Arrays.asList(
                        Category.GENERAL_ESSENTIAL,
                        Category.GENERAL_SELECTIVE
                )));
        TakeResponseDTO.GeneralCoreDTO generalCoreDTO = makePersonalGeneralCoreDTO(creditByCategory,
                untakenCourseDTOList, getStandardCredit(member));

        // 멤버의 통계 정보를 반환
        return TakeConverter.convertToMemberStats(
                takeList,
                takenMajorDTO,
                takenGeneralDTO,
                generalCoreDTO
        );

    }

    @Override

    @Transactional
    // 미이수 과목을 검색하는 메서드
    public TakeResponseDTO.UntakenCourseListDTO searchUntakenCourses(
            Long memberId, TakeRequestDTO.SearchOptionDTO searchOptionDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        log.info("검색어 : " + searchOptionDTO.getCourseName());
        List<Course> untakenList = findByNameOrCode(searchOptionDTO,
                member.getTakeList().stream()
                        .map(take -> take.getCourse().getId())
                        .collect(Collectors.toList()));

        List<TakeResponseDTO.UntakenCourseDTO> resultList = searchByCategories(
                makePersonalizedUntakenCourseList(untakenList, member), searchOptionDTO);

        return TakeConverter.convertUntakeList(resultList);
    }

    // 이름 또는 코드로 미이수 과목을 검색하는 메서드
    private List<Course> findByNameOrCode(TakeRequestDTO.SearchOptionDTO searchUntakenCourseDTO,
                                          List<Long> takenCourseIdList) {
        if (!searchUntakenCourseDTO.getCourseName().isBlank()) {
            String searchWord = searchUntakenCourseDTO.getCourseName();
            return courseRepository.findCourseNotInIdsByCourseName(takenCourseIdList, searchWord);
        }
        if (!searchUntakenCourseDTO.getCourseCode().isBlank()) {
            String searchWord = searchUntakenCourseDTO.getCourseCode();
            return courseRepository.findCourseNotInIdsByCourseCode(takenCourseIdList, searchWord);
        }
        return courseRepository.findCoursesNotInIds(takenCourseIdList);
    }


    // 카테고리별로 미이수 과목을 검색하는 메서드
    private List<TakeResponseDTO.UntakenCourseDTO> searchByCategories(
            List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList,
            TakeRequestDTO.SearchOptionDTO searchOptionDTO) {
        List<TakeResponseDTO.UntakenCourseDTO> resultList = new ArrayList<>();
        if (searchOptionDTO.getIsMajorEssential()) {
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.MAJOR_ESSENTIAL));
        }
        if (searchOptionDTO.getIsMajorSelective()) {
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.MAJOR_SELECTIVE));
        }
        if (searchOptionDTO.getIsGeneralEssential()) {
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_ESSENTIAL));
        }
        if (searchOptionDTO.getIsGeneralCore()) {
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CORE_ONE));
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CORE_TWO));
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CORE_THREE));
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CORE_FOUR));
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CORE_FIVE));
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CORE_SIX));
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_CREATIVE));
        }
        if (searchOptionDTO.getIsGeneralSelective()) {
            resultList.addAll(searchByCategory(untakenCourseDTOList, Category.GENERAL_SELECTIVE));
        }
        return resultList;
    }

    // 특정 카테고리의 미이수 과목을 필터링하는 메서드
    private List<TakeResponseDTO.UntakenCourseDTO> searchByCategory(
            List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList, Category category) {
        return untakenCourseDTOList.stream()
                .filter(untakenCourseDTO -> Category.getCategoryByName(untakenCourseDTO.getCategoryName())
                        .equals(category))
                .toList();
    }

    // 카테고리별 이수 학점을 계산하는 메서드
    private Map<Category, Integer> calculateCreditByCategory(List<Take> takeList) {
        Map<Category, Integer> creditByCategory = takeList.stream()
                .collect(Collectors.groupingBy(
                        Take::getCategory,
                        Collectors.summingInt(take -> take.getCourse().getCredit())
                ));

        for (Category category : Category.values()) {
            creditByCategory.putIfAbsent(category, 0);
        }

        return creditByCategory;
    }

    // 개인화된 교양 과목 DTO를 생성하는 메서드
    private TakeResponseDTO.GeneralCoreDTO makePersonalGeneralCoreDTO(
            Map<Category, Integer> creditByCategory,
            List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList, Integer standardCredit) {
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
                generalCoreCategories.stream()
                        .map(creditByCategory::get) // 각 Category에 대한 학점을 가져옴
                        .toList(),
                filterTop5ByCategories(untakenCourseDTOList, generalCoreCategories),
                standardCredit
        );
    }

    // 개인화된 미이수 과목 리스트를 생성하는 메서드
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


    // 여러 카테고리의 상위 5개 미이수 과목을 필터링하는 메서드
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

    private Integer getStandardCredit(Member member) {
        if (member.isNotSWConvergence()) {
            return 9;
        }
        return 12;
    }
}
