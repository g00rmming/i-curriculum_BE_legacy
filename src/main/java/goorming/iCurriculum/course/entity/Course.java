package goorming.iCurriculum.course.entity;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Length(max = 7, min = 7)
    private String code;

    private String name;

    @Min(value = 0) // 0은 학년 제한 없는 과목
    @Max(value = 4)
    private Integer level;

    private Category category;

    @Min(value = 1)
    @Max(value = 4)
    private Integer credit;

    private Boolean isOpen;

    private Integer takenNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<EssentialCourse> essentialCourseList;

    public void takeThisCourse() {
        takenNumber++;
    }

    public void dropThisCourse() {
        takenNumber--;
    }

}
