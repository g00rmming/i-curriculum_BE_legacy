package goorming.iCurriculum.login.entity;

import goorming.iCurriculum.common.BaseEntity;
import goorming.iCurriculum.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String refreshToken;

    private Date expiredAt;

}
