package goorming.iCurriculum.member.service;


import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.dto.MemberRequestDTO;

public interface MemberService {

    // 멤버 등록
    Member joinMember(MemberRequestDTO.JoinMemberDTO request);

    //username 중복 확인
    Boolean checkClientId(String clientId);

    // nickname 중복 확인
    Boolean checkNickname(String nickname);

    // 멤버 정보 수정 (닉네임, 완료 학기)
    Member updateMember(Long memberId, MemberRequestDTO.UpdateMemberDTO request);

    // 전과한 멤버의 학과변경
    Long changeDepartment(Long memberId, MemberRequestDTO.ChangeMajorDTO request);

    Member findByClientId(String clientId);

    Member findMember(Long memberId);

    //회원 탈퇴 (일정 기간 state만 변경한 상태로 유지)
    Long deleteMember(Long memberId);
}
