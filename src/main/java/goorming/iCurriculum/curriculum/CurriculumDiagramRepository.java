package goorming.iCurriculum.curriculum;

import goorming.iCurriculum.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumDiagramRepository extends JpaRepository<CurriculumDiagram,Long> {
    CurriculumDiagram findByDepartment(Department department);
}
