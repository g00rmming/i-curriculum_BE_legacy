package goorming.iCurriculum.member.stat;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_stat")
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

    @OneToOne
    private Member member;


}
