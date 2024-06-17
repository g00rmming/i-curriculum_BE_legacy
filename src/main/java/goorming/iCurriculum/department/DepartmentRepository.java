package goorming.iCurriculum.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Department findDepartmentByName(String department_name);

    Department findDepartmentByCode(String code);

    @Query("SELECT d.name FROM Department d GROUP BY d.name")
    List<String> findDistinctDepartmentName();
}
