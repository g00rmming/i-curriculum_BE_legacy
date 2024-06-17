package goorming.iCurriculum.department;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Department findDepartmentByName(String department_name);
    Department findDepartmentByCode(String code);
}
