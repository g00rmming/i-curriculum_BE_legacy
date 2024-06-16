package goorming.iCurriculum.member.converter;

import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.dto.MemberRequestDTO;
import goorming.iCurriculum.member.dto.MemberResponseDTO;

import java.time.LocalDateTime;

public class MemberConverter {

    public static MemberResponseDTO.JoinMemberDTO toJoinResultDTO(Member member) {
        return MemberResponseDTO.JoinMemberDTO.builder()
                .memberId(member.getId())
                .clientId(member.getClientId())
                .createAt(LocalDateTime.now())
                .build();
    }

    public static Member toMember(MemberRequestDTO.JoinMemberDTO request) {
        return Member.builder()
                .clientId(request.getClientId().toLowerCase())
                .nickname(request.getNickname().toLowerCase())
                .joinYear(request.getJoinYear())
                .completeTerm(request.getCompleteTerm())
                .build();
    }

    public static MemberResponseDTO.UpdateMemberDTO toUpdateResultDTO(Member member) {
        return MemberResponseDTO.UpdateMemberDTO.builder()
                .memberId(member.getId())
                .updateAt(LocalDateTime.now())
                .build();
    }

    public static MemberResponseDTO.MyInfoDTO myInfoDTO(Member member) {
        return MemberResponseDTO.MyInfoDTO.builder()
                .clientId(member.getClientId())
                .nickname(member.getNickname())
                .department_name(member.getDepartment().getName())
                .completeTerm(member.getCompleteTerm())
                .joinYear(member.getJoinYear())
                .build();
    }

}

