package goorming.iCurriculum.take.entity.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TakeRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTakeDTO {
        @NotNull
        private Long courseId;

        @NotNull
        private Integer takenTerm;

        @NotEmpty
        private String grade;

        @NotEmpty
        private String category;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTakeListDTO {
        List<@Valid CreateTakeDTO> createTakeDTOList;
    }

    @Getter
    @Builder
    public static class UpdateTakenCourseDTO {
        @NotNull
        private Integer takenTerm;

        @NotEmpty
        private String grade;

        @NotNull
        @NotEmpty
        private String category;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class SearchOptionDTO {
        @NotNull
        private String courseName;

        @NotNull
        private String courseCode;

        @NotNull
        private Boolean isMajorEssential;

        @NotNull
        private Boolean isMajorSelective;

        @NotNull
        private Boolean isGeneralEssential;

        @NotNull
        private Boolean isGeneralCore;

        @NotNull
        private Boolean isGeneralSelective;
    }
}
