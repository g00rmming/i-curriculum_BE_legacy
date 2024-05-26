package goorming.iCurriculum.department;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.essentialcourse.EssentialCourse;
import jakarta.persistence.*;

import java.util.ArrayList;
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
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Length(min = 3, max = 3)
    private String code;

    private String name;

    private String url;

    @Length(min = 12, max = 12)
    private String phoneNumber;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<EssentialCourse> essentialCourseList = new ArrayList<>();

}
