package goorming.iCurriculum.course.service.util;

import goorming.iCurriculum.course.entity.Category;
import goorming.iCurriculum.course.entity.Course;
import goorming.iCurriculum.course.entity.dto.CourseRequestDTO;
import goorming.iCurriculum.department.Department;

public class CourseConverter {
    public static Course toCourse(CourseRequestDTO.CreateCourseDTO createCourseDTO, Department department) {
        return Course.builder()
                .name(createCourseDTO.getCourseName())
                .credit(createCourseDTO.getCredit())
                .level(createCourseDTO.getLevel())
                .category(Category.getCategory(
                        getCategory(createCourseDTO.getCourseCode(), createCourseDTO.getCategoryName())))
                .code(createCourseDTO.getCourseCode())
                .department(department)
                .isOpen(true)
                .takenNumber(0)
                .build();
    }

    private static String getCategory(String courseCode, String category) {
        if (category.equals("핵심교양")) {
            category = category + " " + courseCode.charAt(3);
        }
        return category;
    }
}
