package goorming.iCurriculum.curriculum.controller;

import goorming.iCurriculum.common.ApiResponse;
import goorming.iCurriculum.curriculum.dto.CurriculumResponseDTO;
import goorming.iCurriculum.curriculum.service.CurriculumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/curriculum")
public class CurriculumController {
    private final CurriculumService curriculumService;

    @GetMapping("/table")
    @Operation(summary = "교과과정표 API", description =
            "request]\n param : 사용자 id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<CurriculumResponseDTO.CurriculumTableDTO> findCurriculumTable(
            @RequestParam("memberId") Long memberId) {
        CurriculumResponseDTO.CurriculumTableDTO curriculumTableDTO =
                curriculumService.findCurriculumTable(memberId);

        return ApiResponse.onSuccess(curriculumTableDTO);
    }

    @GetMapping("/diagram")
    @Operation(summary = "교과과정표 API", description =
            "request]\n param : 사용자 id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<CurriculumResponseDTO.CurriculumDiagramDTO> findCurriculumDiagram(
            @RequestParam("memberId") Long memberId) {
        CurriculumResponseDTO.CurriculumDiagramDTO curriculumDiagramDTO =
                curriculumService.findCurriculumDiagram(memberId);

        return ApiResponse.onSuccess(curriculumDiagramDTO);
    }
}
