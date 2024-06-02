package goorming.iCurriculum.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course,Long> {
    @Query("SELECT c FROM Course c WHERE c.id IN :ids")
    List<Course> findCoursesByIds(@Param("ids") List<Long> ids);
    @Query("SELECT c FROM Course c WHERE c.id NOT IN :ids ORDER BY c.takenNumber DESC")
    List<Course> findCoursesNotInIds(@Param("ids") List<Long> ids);


