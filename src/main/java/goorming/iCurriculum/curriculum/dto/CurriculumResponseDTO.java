package goorming.iCurriculum.curriculum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CurriculumResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurriculumTableDTO{
        Integer joinYear;
        String url;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurriculumDiagramDTO{
        String url;
    }
}
