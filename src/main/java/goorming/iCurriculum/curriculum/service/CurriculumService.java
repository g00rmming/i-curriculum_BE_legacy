package goorming.iCurriculum.curriculum.service;

import goorming.iCurriculum.common.code.status.ErrorStatus;
import goorming.iCurriculum.common.exception.GeneralException;
import goorming.iCurriculum.curriculum.CurriculumDiagram;
import goorming.iCurriculum.curriculum.CurriculumDiagramRepository;
import goorming.iCurriculum.curriculum.CurriculumTable;
import goorming.iCurriculum.curriculum.CurriculumTableRepository;
import goorming.iCurriculum.curriculum.dto.CurriculumResponseDTO;
import goorming.iCurriculum.department.Department;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CurriculumService {
    private final CurriculumTableRepository curriculumTableRepository;
    private final CurriculumDiagramRepository curriculumDiagramRepository;
    private final MemberRepository memberRepository;

    public CurriculumResponseDTO.CurriculumTableDTO findCurriculumTable(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Department department = member.getDepartment();
        Integer joinYear = member.getJoinYear();

        CurriculumTable curriculumTable = curriculumTableRepository.findByDepartmentAndJoinYear(department, joinYear);
        return CurriculumResponseDTO.CurriculumTableDTO.builder()
                .joinYear(curriculumTable.getJoinYear())
                .url(curriculumTable.getUrl())
                .build();
    }

    public CurriculumResponseDTO.CurriculumDiagramDTO findCurriculumDiagram(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Department department = member.getDepartment();

        CurriculumDiagram curriculumDiagram = curriculumDiagramRepository.findByDepartment(department);

        return CurriculumResponseDTO.CurriculumDiagramDTO.builder()
                .url(curriculumDiagram.getUrl())
                .build();
    }

}
