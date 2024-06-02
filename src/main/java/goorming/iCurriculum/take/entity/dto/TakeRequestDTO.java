package goorming.iCurriculum.take.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class TakeRequestDTO {
    @Getter
    @Builder
    public static class CreateTakeDTO {
        @NotNull
        private Long courseId;

        @NotNull
        private Integer takenTerm;

        @NotEmpty
        private String grade;

        @NotNull
        private String category;
    }

    @Getter
    @Builder
    public static class CreateTakeListDTO {
        List<CreateTakeDTO> takeCourseDTOList;
    }

    @Getter
    @Builder
    public static class UpdateTakenCourseDTO {
        @NotNull
        private Integer takenTerm;

        @NotEmpty
        private String grade;
    }
}
