package goorming.iCurriculum.take.service.util;

import goorming.iCurriculum.course.Category;
import goorming.iCurriculum.course.Course;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.take.entity.Grade;
import goorming.iCurriculum.take.entity.Take;
import goorming.iCurriculum.take.entity.dto.TakeRequestDTO;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TakeConverter {
    public static TakeResponseDTO.TakenCourseDTO convertTake(Take take) {
        Course course = take.getCourse();
        return TakeResponseDTO.TakenCourseDTO.builder()
                .takeId(take.getId())
                .courseId(course.getId())
                .courseCode(course.getCode())
                .courseName(course.getName())
                .grade(take.getGrade().getString())
                .takenLevel(take.getTakenLevel())
                .credit(course.getCredit())
                .categoryName(take.getCategory().getName())
                .build();
    }

    public static TakeResponseDTO.TakenCourseListDTO convertTakeList(List<Take> takeList) {
        List<TakeResponseDTO.TakenCourseDTO> takenCourseDTOList = takeList.stream()
                .map(TakeConverter::convertTake)
                .collect(Collectors.toList());

        return TakeResponseDTO.TakenCourseListDTO.builder()
                .takenCourseDTOList(takenCourseDTOList)
                .build();
    }

    public static TakeResponseDTO.UntakenCourseDTO convertUntake(Course course, Category category) {
        return TakeResponseDTO.UntakenCourseDTO.builder()
                .courseId(course.getId())
                .courseCode(course.getCode())
                .courseName(course.getName())
                .level(course.getLevel())
                .credit(course.getCredit())
                .categoryName(category.getName())
                .build();
    }

    public static TakeResponseDTO.UntakenCourseListDTO convertUntakeList(
            List<TakeResponseDTO.UntakenCourseDTO> untakenCourseDTOList) {
        return TakeResponseDTO.UntakenCourseListDTO.builder()
                .untakenCourseDTOList(untakenCourseDTOList)
                .build();
    }

    public static TakeResponseDTO.TakenCategoryDTO convertPersonalCategoryCourseList(Integer totalCredit,
                                                                                     List<TakeResponseDTO.UntakenCourseDTO> untakenTop5CourseList) {
        return TakeResponseDTO.TakenCategoryDTO.builder()
                .takenCredit(totalCredit)
                .untakenTop5CourseDTOList(untakenTop5CourseList)
                .build();
    }

    public static TakeResponseDTO.GeneralCoreDTO convertPersonalGeneralCoreCourse(
            TakeResponseDTO.TakenCategoryDTO takenCategoryDTO, List<Integer> takeGeneralCoreCategory) {
        return TakeResponseDTO.GeneralCoreDTO.builder()
                .takenCategoryDTO(takenCategoryDTO)
                .takeOne(takeGeneralCoreCategory.get(0))
                .takeTwo(takeGeneralCoreCategory.get(1))
                .takeThree(takeGeneralCoreCategory.get(2))
                .takeFour(takeGeneralCoreCategory.get(3))
                .takeFive(takeGeneralCoreCategory.get(4))
                .takeSix(takeGeneralCoreCategory.get(5))
                .takeCreative(takeGeneralCoreCategory.get(6))
                .build();
    }

    public static TakeResponseDTO.MemberStatsDTO convertToMemberStats(
            List<Take> takeList,
            TakeResponseDTO.TakenCategoryDTO majorEssentialDTO,
            TakeResponseDTO.TakenCategoryDTO majorSelectiveDTO,
            TakeResponseDTO.TakenCategoryDTO generalEssentialDTO,
            TakeResponseDTO.TakenCategoryDTO generalSelectiveDTO,
            TakeResponseDTO.GeneralCoreDTO generalCoreDTO
    ) {
        Integer currentTerm = takeList.stream()
                .mapToInt(Take::getTakenTerm) // takenTerm을 int로 매핑
                .max() // 최대값 계산
                .orElse(0);
        return TakeResponseDTO.MemberStatsDTO.builder()
                .totalTakenCredit(takeList.stream()
                        .mapToInt(take -> take.getCourse().getCredit())
                        .sum())
                .majorTakenCredit(takeList.stream()
                        .filter(take -> take.getCategory().equals(Category.MAJOR_ESSENTIAL)
                                || take.getCategory().equals(Category.MAJOR_SELECTIVE))
                        .mapToInt(take -> take.getCourse().getCredit())
                        .sum())
                .totalGrade(takeList.stream()
                        .mapToDouble(take -> take.getGrade().getScore()) // 각 Take 객체의 등급을 int로 매핑
                        .average() // 평균 계산
                        .orElse(0.0))
                .majorGrade(takeList.stream()
                        .filter(take -> take.getCategory().equals(Category.MAJOR_ESSENTIAL)
                                || take.getCategory().equals(Category.MAJOR_SELECTIVE))
                        .mapToDouble(take -> take.getGrade().getScore()) // 각 Take 객체의 등급 점수를 double로 매핑
                        .average() // 평균 계산
                        .orElse(0.0))
                .previousTotalGrade(takeList.stream()
                        .filter(take -> !take.getTakenTerm().equals(currentTerm))
                        .mapToDouble(take -> take.getGrade().getScore()) // 각 Take 객체의 등급을 int로 매핑
                        .average() // 평균 계산
                        .orElse(0.0))
                .previousMajorGrade(takeList.stream()
                        .filter(take -> !take.getTakenTerm().equals(currentTerm))
                        .filter(take -> take.getCategory().equals(Category.MAJOR_ESSENTIAL)
                                || take.getCategory().equals(Category.MAJOR_SELECTIVE))
                        .mapToDouble(take -> take.getGrade().getScore()) // 각 Take 객체의 등급 점수를 double로 매핑
                        .average() // 평균 계산
                        .orElse(0.0))
                .majorEssentialDTO(majorEssentialDTO)
                .majorSelectiveDTO(majorSelectiveDTO)
                .generalEssentialDTO(generalEssentialDTO)
                .generalSelectiveDTO(generalSelectiveDTO)
                .generalCoreDTO(generalCoreDTO)
                .build();
    }

    public static Take toTake(TakeRequestDTO.CreateTakeDTO takeDTO, Course course, Member member) {
        return Take.builder()
                .grade(Grade.getGrade(takeDTO.getGrade()))
                .takenTerm(takeDTO.getTakenTerm())
                .course(course)
                .member(member)
                .category(Category.getCategory(takeDTO.getCategory()))
                .build();
    }
}
