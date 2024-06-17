package goorming.iCurriculum.take.controller;

import goorming.iCurriculum.common.ApiResponse;
import goorming.iCurriculum.common.code.status.ErrorStatus;
import goorming.iCurriculum.take.entity.dto.TakeRequestDTO;
import goorming.iCurriculum.take.entity.dto.TakeResponseDTO;
import goorming.iCurriculum.take.exception.TakeException;
import goorming.iCurriculum.take.service.TakeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/courses")
public class TakeController {
    private final TakeService takeService;

    // 수강 과목 여러개 추가
    @PostMapping("/take/new")
    @Operation(summary = "수강과목 추가 API", description =
            "request]\n param : userid,\nbody : 수업id, 수강학기, 성적\n" +
                    "response] \n수강id, 수업id, 학수번호, 수업이름, 성적, 수강학년, 학점, 영역")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COURSE4001", description = "수업이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "TAKE4003", description = "파라미터 바인딩 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "TAKE4002", description = "중복된 수강내역 입니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<String> createTake(
            @RequestParam("memberId") Long memberId,
            @RequestBody @Valid TakeRequestDTO.CreateTakeListDTO createTakeListDTO,
            BindingResult bindingResult) {

        log.info("enter TakeController : [post] /api/v1/courses/take/new");
        if (bindingResult.hasErrors()) {
            throw new TakeException(ErrorStatus.TAKE_BINDING_FAIL);
        }

        takeService.takeCourse(memberId, createTakeListDTO);

        return ApiResponse.onSuccess("생성 성공");
    }

    // 수강과목 수정
    @PatchMapping("/take/{takeId}")
    @Operation(summary = "수강과목 수정 API", description =
            "request]\n param : 수강 id,\nbody : 수강학기, 성적\n" +
                    "response]\n수강id, 수업id, 학수번호, 수업이름, 성적, 수강학년, 학점, 영역")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COURSE4001", description = "수업이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "TAKE4001", description = "수강내역이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "TAKE4003", description = "파라미터 바인딩 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<TakeResponseDTO.TakenCourseDTO> updateTake(
            @PathVariable Long takeId,
            @RequestBody @Valid TakeRequestDTO.UpdateTakenCourseDTO updateTakenCourseDTO, BindingResult bindingResult) {
        TakeResponseDTO.TakenCourseDTO takenCourseDTO = takeService.updateTake(takeId, updateTakenCourseDTO);

        log.info("enter TakeController : [patch] /api/v1/courses/take/" + takeId);
        if (bindingResult.hasErrors()) {
            throw new TakeException(ErrorStatus.TAKE_BINDING_FAIL);
        }

        return ApiResponse.onSuccess(takenCourseDTO);
    }

    // 수강과목 삭제
    @DeleteMapping("/take/{takeId}")
    @Operation(summary = "수강과목 삭제 API", description =
            "request]\n param : 수강 id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "TAKE4001", description = "수강내역이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<String> deleteTake(
            @PathVariable Long takeId) {
        takeService.deleteTake(takeId);

        return ApiResponse.onSuccess("삭제 성공");
    }

    // 수강내역 전체조회
    @GetMapping("/take")
    @Operation(summary = "기이수 과목 전체 조회 API", description =
            "request]\n param : 사용자 id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<TakeResponseDTO.TakenCourseListDTO> findTakenList(@RequestParam("memberId") Long memberId) {
        TakeResponseDTO.TakenCourseListDTO takenCourseListDTO = takeService.findTakeList(memberId);

        return ApiResponse.onSuccess(takenCourseListDTO);
    }

    // 미수강내역 전체조회
    @GetMapping("/untake")
    @Operation(summary = "미이수 과목 전체 조회 API", description =
            "request]\n param : 사용자 id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<TakeResponseDTO.UntakenCourseListDTO> findUntakenList(@RequestParam("memberId") Long memberId) {
        log.info("TakeController : [get] /api/v1/courses/untake");
        TakeResponseDTO.UntakenCourseListDTO untakenCourseListDTO = takeService.findUntakenList(memberId);

        return ApiResponse.onSuccess(untakenCourseListDTO);
    }


    @PostMapping("/untake/search")
    @Operation(summary = "미이수 검색 API", description =
            "request]\n param : 사용자 id, 검색 옵션")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse
                    (responseCode = "MEMBER4001", description = "사용자가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<TakeResponseDTO.UntakenCourseListDTO> searchUntakenCourse(
            @RequestParam("memberId") Long memberId,
            @RequestBody @Valid TakeRequestDTO.SearchOptionDTO searchOptionDTO,
            BindingResult bindingResult) {

        log.info("search TakeController : [post] /api/v1/untake/search/");
        if (bindingResult.hasErrors()) {
            throw new TakeException(ErrorStatus.COURSE_BINDING_FAIL);
        }


        TakeResponseDTO.UntakenCourseListDTO searchResultListDTO = takeService.searchUntakenCourses(memberId,
                searchOptionDTO);
        return ApiResponse.onSuccess(searchResultListDTO);
    }

}
