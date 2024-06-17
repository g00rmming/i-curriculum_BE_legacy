package goorming.iCurriculum.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinMemberDTO {
        Long memberId;
        String clientId;
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
        String clientId;
        String nickname;
        String department_name;
        Integer completeTerm;
        Integer joinYear;
    }



}

