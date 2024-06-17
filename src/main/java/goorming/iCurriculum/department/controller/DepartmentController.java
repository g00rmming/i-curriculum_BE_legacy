package goorming.iCurriculum.department.controller;

import goorming.iCurriculum.common.ApiResponse;
import goorming.iCurriculum.department.dto.DepartmentResponseDTO;
import goorming.iCurriculum.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/api/v1/departments-names")
    public ApiResponse<DepartmentResponseDTO.findAllDepartmentNameDTO> getDepartmentNames() {
           List<String> departmentNameList = departmentService.findAllDepartmentName();

            return ApiResponse.onSuccess(DepartmentResponseDTO.findAllDepartmentNameDTO.builder()
                    .departmentNameList(departmentNameList)
                    .build());
    }
}
