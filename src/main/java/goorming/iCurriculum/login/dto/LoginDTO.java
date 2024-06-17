package goorming.iCurriculum.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginDTO {
    private String username;
    private Long memberId;

    public LoginDTO(String username, Long memberId) {
        this.username = username;
        this.memberId = memberId;
    }
}
