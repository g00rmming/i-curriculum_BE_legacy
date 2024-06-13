package goorming.iCurriculum.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.cognitoidentityprovider.endpoints.internal.Value;

import java.time.LocalDateTime;

public class MemberResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinMemberDTO {
        Long memberId;
        LocalDateTime createAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberDTO {
        Long memberId;
        LocalDateTime updateAt;
    }



    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    //내 정보 조회 DTO
    public static class MyInfoDTO {
        String username;
        String nickname;
        String department_name;
        Integer completeTerm;
        Integer joinYear;
    }



}

