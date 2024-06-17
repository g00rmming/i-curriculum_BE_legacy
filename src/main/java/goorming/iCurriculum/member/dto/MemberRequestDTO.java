package goorming.iCurriculum.member.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

public class MemberRequestDTO {

    @Data
    @AllArgsConstructor
    public static class JoinMemberDTO {
        @NotNull
        String clientId;
        @NotNull
        String password;
        @NotNull
        String nickname;
        Integer joinYear;
        Integer completeTerm;
        String department_name;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateMemberDTO {
        String nickname;
        Integer completeTerm;
    }

    @Data
    @AllArgsConstructor
    public static class ChangeMajorDTO {
        String department_name;
    }
}
