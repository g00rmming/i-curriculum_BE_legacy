package goorming.iCurriculum.course.repository;

import goorming.iCurriculum.course.entity.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

    public interface CourseRepository extends JpaRepository<Course,Long> {
        @Query("SELECT c FROM Course c WHERE c.id IN :ids")
        List<Course> findCoursesByIds(@Param("ids") List<Long> ids);
        @Query("SELECT c FROM Course c WHERE c.id NOT IN :ids ORDER BY c.takenNumber DESC")
        List<Course> findCoursesNotInIds(@Param("ids") List<Long> ids);

        @Query("SELECT c FROM Course c WHERE c.id NOT IN :ids AND c.name LIKE concat('%', :searchword, '%')")
        List<Course> findCourseNotInIdsByCourseName(@Param("ids") List<Long> ids, @Param("searchword") String searchWord);

        @Query("SELECT c FROM Course c WHERE c.id NOT IN :ids AND c.code LIKE concat('%', :searchword, '%')")
        List<Course> findCourseNotInIdsByCourseCode(@Param("ids") List<Long> ids, @Param("searchword") String searchWord);

}
