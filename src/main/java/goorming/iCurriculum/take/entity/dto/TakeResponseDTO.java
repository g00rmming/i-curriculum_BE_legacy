package goorming.iCurriculum.take.entity.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class TakeResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TakenCourseDTO{
        private Long takeId;
        private Long courseId;
        private String courseCode;
        private String courseName;
        private String grade;
        private Integer takenLevel;
        private Integer credit;
        private String categoryName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TakenCourseListDTO{
        List<TakenCourseDTO> takenCourseDTOList;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UntakenCourseDTO{
        private Long courseId;
        private String courseCode;
        private String courseName;
        private Integer level;//학년
        private Integer credit;
        private String categoryName;
        private Integer takenNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UntakenCourseListDTO{
        List<UntakenCourseDTO> untakenCourseDTOList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DashboardDTO {
        private Integer totalTakenCredit;
        private Integer majorTakenCredit;
        private Double totalGrade;
        private Double majorGrade;
        private Double previousTotalGrade;
        private Double previousMajorGrade;
        private TakenCategoryDTO takenMajorDTO;
        private TakenCategoryDTO takenGeneralDTO;
        private GeneralCoreDTO generalCoreDTO;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TakenCategoryDTO{
        private Integer takenEssentialCredit;
        private Integer takenSelectiveCredit;
        private List<UntakenCourseDTO> untakenTop5CourseDTOList;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralCoreDTO{
        private Integer takeOne;
        private Integer takeTwo;
        private Integer takeThree;
        private Integer takeFour;
        private Integer takeFive;
        private Integer takeSix;
        private Integer takeCreative;
        private Integer totalCredit;
        private Integer standardCredit;
        private List<UntakenCourseDTO> untakenTop5CourseDTOList;
    }
}
