package goorming.iCurriculum.member;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.take.entity.Take;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Length(min = 4, max = 20)
    private String clientId;

    @Length(min = 8)
    private String password;

    @Length(max = 20)
    @NotEmpty
    private String nickname;

    private Integer joinYear;

    @Min(value = 0)
    @Max(value = 8)
    private Integer completeTerm;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Take> takeList = new ArrayList<>();

    public Boolean isNotSWConvergence(){
        return joinYear < 2021;
    }

}
