package goorming.iCurriculum.member.stat;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_stat_id")
    private Long id;

    private Integer totalCredit;
    private Integer majorCredit;
    private Integer coreOne;
    private Integer coreTwo;
    private Integer coreThree;
    private Integer coreFour;
    private Integer coreFive;
    private Integer coreSix;
    private Integer creative;

    @Column(name = "grade_1_1")
    private Double grade11;
    @Column(name = "grade_1_2")
    private Double grade12;
    @Column(name = "grade_2_1")
    private Double grade21;
    @Column(name = "grade_2_2")
    private Double grade22;
    @Column(name = "grade_3_1")
    private Double grade31;
    @Column(name = "grade_3_2")
    private Double grade32;
    @Column(name = "grade_4_1")
    private Double grade41;
    @Column(name = "grade_4_2")
    private Double grade42;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
