package goorming.iCurriculum.course.entity.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CourseRequestDTO {

    @Getter
    @Builder
    public static class CreateCourseDTO {
        String deptName;

        String courseCode;

        String name;

        Integer grade;

        Integer credit;

        String category;

        String deptCode;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCourseListDTO {
        String deptName;
        List<@Valid CreateCourseDTO> createCourseDTOList;
    }

}
