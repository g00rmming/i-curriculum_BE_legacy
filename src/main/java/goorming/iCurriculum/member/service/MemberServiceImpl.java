package goorming.iCurriculum.member.service;

import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.department.DepartmentRepository;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.MemberRepository;
import goorming.iCurriculum.member.RoleType;
import goorming.iCurriculum.member.converter.MemberConverter;
import goorming.iCurriculum.member.dto.MemberRequestDTO;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Data
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinMemberDTO request) {
        Department department = departmentRepository.findDepartmentByName(request.getDepartment_name());
        Member newMember = MemberConverter.toMember(request);
        //사용자의 학과 등록
        newMember.setDepartment(department);
        //일반 회원으로 가입
        newMember.setRoleType(RoleType.ADMIN);
        newMember.setCreatedAt(LocalDateTime.now());
        newMember.setUpdatedAt(LocalDateTime.now());
        //비밀번호 brcypt 암호화하여 등록
        newMember.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

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

