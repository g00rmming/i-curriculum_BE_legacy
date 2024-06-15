package goorming.iCurriculum.course.service.util;

import goorming.iCurriculum.course.entity.Category;
import goorming.iCurriculum.course.entity.Course;
import goorming.iCurriculum.course.entity.dto.CourseRequestDTO;
import goorming.iCurriculum.department.Department;

public class CourseConverter {
    public static Course toCourse(CourseRequestDTO.CreateCourseDTO createCourseDTO, Department department) {
        System.out.println(createCourseDTO.getCourseCode());
        return Course.builder()
                .name(createCourseDTO.getName())
                .credit(createCourseDTO.getCredit())
                .level(createCourseDTO.getGrade())
                .category(Category.getCategoryByName(
                        parseCategory(createCourseDTO.getCourseCode(), createCourseDTO.getCategory())))
                .code(createCourseDTO.getCourseCode())
                .department(department)
                .isOpen(true)
                .takenNumber(0)
                .build();
    }

    private static String parseCategory(String courseCode, String category) {
        if (courseCode.contains("GED")) {
            return "핵심교양 " + courseCode.charAt(3);
        }
        if (courseCode.contains("GEE") && courseCode.charAt(3) == '4') {
            return "창의영역";
        }
        return category;
    }
}
