package goorming.iCurriculum.take.service.util;

import goorming.iCurriculum.course.entity.Category;
import goorming.iCurriculum.course.entity.Course;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import goorming.iCurriculum.member.Member;
import java.util.Arrays;
import java.util.List;

public class CourseFilter {
    // GENERAL_CORE 카테고리 배열 정의
    private static final Category[] GENERAL_CORE = {
            Category.GENERAL_CORE_ONE,
            Category.GENERAL_CORE_TWO,
            Category.GENERAL_CORE_THREE,
            Category.GENERAL_CORE_FOUR,
            Category.GENERAL_CORE_FIVE,
            Category.GENERAL_CORE_SIX
    };

    // 과목을 필터링하여 해당 카테고리를 반환하는 메서드
    public static Category filterCourse(Member member, Course course,
                                        List<EssentialCourse> departmentEssentialCourseList) {
        if (isMajorEssential(member, course)) {
            return Category.MAJOR_ESSENTIAL;
        }
        if (isMajorSelective(member, course)) {
            return Category.MAJOR_SELECTIVE;
        }
        if (isGeneralEssential(course, departmentEssentialCourseList)) {
            return Category.GENERAL_ESSENTIAL;
        }
        if (isGeneralCore(member, course)) {
            return course.getCategory();
        }
        return Category.GENERAL_SELECTIVE;
    }

    // 전공 필수 과목인지 확인하는 메서드
    private static Boolean isMajorEssential(Member member, Course course) {
        if (!equalDepartment(member, course)) {
            return false;
        }
        return course.getCategory().equals(Category.MAJOR_ESSENTIAL);
    }

    // 전공 선택 과목인지 확인하는 메서드
    private static Boolean isMajorSelective(Member member, Course course) {
        if (!equalDepartment(member, course)) {
            return false;
        }
        return course.getCategory().equals(Category.MAJOR_SELECTIVE);
    }

    // 교양 필수 과목인지 확인하는 메서드
    private static Boolean isGeneralEssential(Course course,
                                              List<EssentialCourse> departmentEssentialCourseList) {
        return departmentEssentialCourseList.stream()
                .anyMatch(essentialCourse -> essentialCourse.getCourse().equals(course));
    }

    // 교양 핵심 과목인지 확인하는 메서드
    private static Boolean isGeneralCore(Member member, Course course) {
        if(notCoreByMember(member,course))
            return false;
        if (member.isNotSWConvergence()) {
            return Arrays.stream(GENERAL_CORE)
                    .anyMatch(c -> course.getCategory().equals(c));
        }
        if (course.getCategory().equals(Category.GENERAL_CORE_ONE)) {
            return true;
        }
        if (course.getCategory().equals(Category.GENERAL_CORE_TWO)) {
            return true;
        }
        if (course.getCategory().equals(Category.GENERAL_CORE_FOUR)) {
            return true;
        }
        if (course.getCategory().equals(Category.GENERAL_CORE_SIX)) {
            return true;
        }
        return course.getCategory().equals(Category.GENERAL_CREATIVE);
    }

    // 멤버의 학과와 과목의 학과가 동일한지 확인하는 메서드
    private static Boolean equalDepartment(Member member, Course course) {
        return member.getDepartment().equals(course.getDepartment());
    }

    // 특정 멤버에게 핵심 교양인지 확인하는 메서드
    private static Boolean notCoreByMember(Member member, Course course) {
        if (member.isNotSWConvergence()) { // 21보다 선배인 경우
            Integer takenCoreCredit = member.getTakeList().stream()
                    .filter(take -> Arrays.stream(GENERAL_CORE).toList().contains(take.getCategory()))
                    .mapToInt(take -> take.getCourse().getCredit())
                    .sum();
            return takenCoreCredit >= 9;
        }

        return !(member.getTakeList().stream()
                .filter(take -> take.getCategory().equals(course.getCategory()))
                .toList()
                .isEmpty());
    }
}
