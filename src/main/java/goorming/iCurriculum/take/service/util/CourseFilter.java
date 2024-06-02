package goorming.iCurriculum.take.service.util;

import goorming.iCurriculum.course.Category;
import goorming.iCurriculum.course.Course;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import goorming.iCurriculum.member.Member;
import java.util.Arrays;
import java.util.List;




public class CourseFilter {
    private static final Category[] GENERAL_CORE = {
            Category.GENERAL_CORE_ONE,
            Category.GENERAL_CORE_TWO,
            Category.GENERAL_CORE_THREE,
            Category.GENERAL_CORE_FOUR,
            Category.GENERAL_CORE_FIVE,
            Category.GENERAL_CORE_SIX
    };

    public static Category filterCourse(Member member, Course course,
                                        List<EssentialCourse> departmentEssentialCourseList) {
        if (isMajorEssential(member, course)) {
            return Category.MAJOR_ESSENTIAL;
        }
        if (isMajorSelective(member, course)) {
            return Category.MAJOR_ESSENTIAL;
        }
        if (isGeneralEssential(member, course, departmentEssentialCourseList)) {
            return Category.GENERAL_ESSENTIAL;
        }
        if (isGeneralCore(member, course)) {
            return course.getCategory();
        }
        return Category.GENERAL_SELECTIVE;
    }

    private static Boolean isMajorEssential(Member member, Course course) {
        if (!equalDepartment(member, course)) {
            return false;
        }
        return course.getCategory().equals(Category.MAJOR_ESSENTIAL);
    }

    private static Boolean isMajorSelective(Member member, Course course) {
        if (!equalDepartment(member, course)) {
            return false;
        }
        return course.getCategory().equals(Category.MAJOR_SELECTIVE);
    }

    private static Boolean isGeneralEssential(Member member, Course course,
                                              List<EssentialCourse> departmentEssentialCourseList) {
        return departmentEssentialCourseList.stream()
                .anyMatch(essentialCourse -> essentialCourse.getCourse().equals(course));
    }

    private static Boolean isGeneralCore(Member member, Course course) {
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

    private static Boolean equalDepartment(Member member, Course course) {
        return member.getDepartment().equals(course.getDepartment());
    }
}
