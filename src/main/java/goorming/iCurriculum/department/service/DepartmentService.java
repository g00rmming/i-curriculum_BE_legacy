package goorming.iCurriculum.department.service;

import goorming.iCurriculum.department.DepartmentRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<String> findAllDepartmentName() {
        return departmentRepository.findDistinctDepartmentName();
    }
}
