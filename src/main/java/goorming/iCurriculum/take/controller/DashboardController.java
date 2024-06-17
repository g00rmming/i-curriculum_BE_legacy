package goorming.iCurriculum.take.controller;

import goorming.iCurriculum.common.ApiResponse;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO.DashboardDTO;
import goorming.iCurriculum.take.service.TakeService;
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
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final TakeService takeService;

    @GetMapping("")
    @Operation(summary = "대시보드 API", description =
            "request] param : 사용자 id"
                    + "response] "
                    + "\t전체 이수 학점"
                    + "\t전공 이수 학점"
                    + "\t전체 성적"
                    + "\t전공 성적"
                    + "\t직전학기 전체 성적"
                    + "\t직전학기 전공 성적"
                    + "\t영역별 이수학점"
                    + "\t영역별 미이수 과목 top5")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<DashboardDTO> findMemberStat(@RequestParam Long memberId) {
        DashboardDTO dashboardDTO = takeService.findMemberStat(memberId);

        return ApiResponse.onSuccess(dashboardDTO);
    }
}
