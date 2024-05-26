package goorming.iCurriculum.member;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.mapping.take.Take;
import goorming.iCurriculum.member.stat.MemberStat;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Length(min = 4, max = 20)
    private String clientId;

    @Length(min = 8, max = 20)
    private String password;

    @Length(max = 20)
    @NotEmpty
    private String nickname;

    private Integer joinYear;

    @Min(value = 0) @Max(value = 8)
    private Integer completeTerm;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Take> takeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberStat memberStat;
}
