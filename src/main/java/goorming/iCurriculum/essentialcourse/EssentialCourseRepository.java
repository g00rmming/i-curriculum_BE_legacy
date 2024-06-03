package goorming.iCurriculum.essentialcourse;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssentialCourseRepository extends JpaRepository<EssentialCourse,Long> {
    List<EssentialCourse>findByDepartmentId(Long DepartmentId);
}
