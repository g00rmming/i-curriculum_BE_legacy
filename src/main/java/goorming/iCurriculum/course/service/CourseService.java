package goorming.iCurriculum.course.service;

import goorming.iCurriculum.course.entity.Category;
import goorming.iCurriculum.course.entity.Course;
import goorming.iCurriculum.course.entity.dto.CourseRequestDTO;
import goorming.iCurriculum.course.repository.CourseRepository;
import goorming.iCurriculum.course.service.util.CourseConverter;
import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.department.DepartmentRepository;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import goorming.iCurriculum.essentialcourse.EssentialCourseRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EssentialCourseRepository essentialCourseRepository;

    @Transactional
    public void saveCourse(CourseRequestDTO.CreateCourseListDTO createCourseListDTO) {
        List<Course> courseList = createCourseListDTO.getCreateCourseDTOList().stream()
                .map(createCourseDTO -> CourseConverter.toCourse(createCourseDTO, getDepartment(
                        createCourseDTO.getDeptCode())))
                .toList();
        courseRepository.saveAll(courseList);

        Department department = departmentRepository.findDepartmentByName(createCourseListDTO.getDeptName());
        saveEssentialCourse(courseList, department);
    }

    private Department getDepartment(String deptCode) {
        System.out.println(deptCode);
        Department department = departmentRepository.findDepartmentByCode(deptCode);
        System.out.println(department.getCode());
        return department;
    }

    private void saveEssentialCourse(List<Course> courseList, Department department) {
        List<EssentialCourse> essentialCourseList = courseList.stream()
                .filter(course -> course.getCategory().equals(Category.GENERAL_ESSENTIAL)
                        || course.getCategory().equals(Category.MAJOR_ESSENTIAL))
                .map(course -> createEssentialCourse(course, department))
                .collect(Collectors.toList());

        essentialCourseRepository.saveAll(essentialCourseList);
    }

    private EssentialCourse createEssentialCourse(Course course, Department department) {
        return EssentialCourse.builder()
                .course(course)
                .department(department)
                .isMajor(course.getCategory().equals(Category.MAJOR_ESSENTIAL))
                .build();
    }
}
