package goorming.iCurriculum.member.service;

import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.department.DepartmentRepository;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.MemberRepository;
import goorming.iCurriculum.member.RoleType;
import goorming.iCurriculum.member.controller.MemberController;
import goorming.iCurriculum.member.converter.MemberConverter;
import goorming.iCurriculum.member.dto.MemberRequestDTO;
import goorming.iCurriculum.member.dto.MemberResponseDTO;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Data
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinMemberDTO request) {
        String depatment_name = request.getDepartment_name();
        Department department = departmentRepository.findDepartmentByName(depatment_name);
        System.out.println(department.getName());
        Member newMember = MemberConverter.toMember(request);
        newMember.setDepartment(department);
        //일반 회원으로 가입
        newMember.setRoleType(RoleType.NORMAL);
        newMember.setCreatedAt(LocalDateTime.now());
        newMember.setUpdatedAt(LocalDateTime.now());

        return memberRepository.save(newMember);
    }

    @Override
    public Boolean checkClientId(String clientId) {
        return memberRepository.existsByClientId(clientId.toLowerCase());
    }

    @Override
    public Boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname.toLowerCase());
    }

    @Override
    @Transactional
    public Member updateMember(Long memberId, MemberRequestDTO.UpdateMemberDTO request) {
        Member member = findMember(memberId);
        member.setNickname(request.getNickname());
        member.setUpdatedAt(LocalDateTime.now());
        return member;
    }

    @Override
    @Transactional
    public Long changeDepartment(Long memberId, MemberRequestDTO.ChangeMajorDTO request) {
        Member member = findMember(memberId);
        Department department = departmentRepository.findDepartmentByName(request.getDepartment_name());
        member.setDepartment(department);
        return memberId;
    }

    @Override
    public Long deleteMember(Long memberId) {
        Member member = findMember(memberId);
        memberRepository.delete(member);
        return member.getId();
    }

    @Override
    public Member findByClientId(String clientId) {
        return memberRepository.findByClientId(clientId);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(
                "해당 멤버가 없습니다. memberId = " + memberId
        ));
    }
}

