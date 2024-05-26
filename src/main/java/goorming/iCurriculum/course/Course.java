package goorming.iCurriculum.course;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.mapping.take.Take;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.Length;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
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

    private String category;

    @Min(value = 1)
    @Max(value = 4)
    private Integer credit;

    private Boolean isOpen;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Take> takeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_id")
    private Course course;

}
