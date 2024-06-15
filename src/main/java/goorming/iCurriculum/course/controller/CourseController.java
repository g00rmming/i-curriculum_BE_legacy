package goorming.iCurriculum.course.controller;

import goorming.iCurriculum.common.ApiResponse;
import goorming.iCurriculum.common.code.status.ErrorStatus;
import goorming.iCurriculum.course.entity.dto.CourseRequestDTO;
import goorming.iCurriculum.course.exception.CourseException;
import goorming.iCurriculum.course.service.CourseService;
import goorming.iCurriculum.take.entity.dto.TakeRequestDTO;
import goorming.iCurriculum.take.exception.TakeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    @PostMapping("/new")
    @Operation(summary = "과목 추가 API", description =
            "request]\n String deptName;\n"
                    + " String courseCode;\n"
                    + " String courseName;\n"
                    + " Integer level;\n"
                    + " Integer credit;\n"
                    + " String categoryName;")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TAKE4003", description = "파라미터 바인딩 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<String> createCourse(
            @RequestBody @Valid CourseRequestDTO.CreateCourseListDTO createCourseListDTO,
            BindingResult bindingResult) {

        log.info("enter TakeController : [post] /api/v1/courses/new");
        if (bindingResult.hasErrors()) {
            throw new CourseException(ErrorStatus.COURSE_BINDING_FAIL);
        }
        courseService.saveCourse(createCourseListDTO);

        return ApiResponse.onSuccess("생성 성공");
    }
}

