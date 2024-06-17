package goorming.iCurriculum.curriculum;

import goorming.iCurriculum.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumTableRepository extends JpaRepository<CurriculumTable,Long> {
    CurriculumTable findByDepartmentAndJoinYear(Department department, Integer joinYear);
}
