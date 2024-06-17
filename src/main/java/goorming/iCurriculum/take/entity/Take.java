package goorming.iCurriculum.take.entity;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.course.entity.Category;
import goorming.iCurriculum.course.entity.Course;
import goorming.iCurriculum.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Take extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "take_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Min(value = 1)
    @Max(value = 8)
    private Integer takenTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;
    public Integer getTakenLevel() {
        return (takenTerm + 1) / 2;
    }
    public Take update(Integer takenTerm, Grade grade, Category category){
        this.takenTerm = takenTerm;
        this.grade = grade;
        this.category = category;
        return this;
    }

}
