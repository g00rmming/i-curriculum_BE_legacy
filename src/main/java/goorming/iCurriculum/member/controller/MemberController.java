package goorming.iCurriculum.member.controller;

import goorming.iCurriculum.common.ApiResponse;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.converter.MemberConverter;
import goorming.iCurriculum.member.dto.MemberRequestDTO;
import goorming.iCurriculum.member.dto.MemberResponseDTO;
import goorming.iCurriculum.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    @Operation(summary = "회원 등록 api", description = "회원가입하는 api. username / password / nickname / joinYeear " +
            "/ completefTerm / department_name ( 학과 이름 넣기 ) ")
    @Parameters({
            @Parameter(name = "clientId"),
            @Parameter(name = "password"),
            @Parameter(name = "nickname"),
            @Parameter(name = "joinYear"),
            @Parameter(name = "completeTerm"),
            @Parameter(name = "department_name")
    })
    public ApiResponse<MemberResponseDTO.JoinMemberDTO> join(@RequestBody @Valid MemberRequestDTO.JoinMemberDTO request) {
        Member member = memberService.joinMember(request);

        return ApiResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    @GetMapping("/isExistId")
    @Operation(summary = "clidentId 중복체크 API", description = "중복여부 True/False 반환하는 api" +
            "/ 중복이면 TRUE, 중복이 아니면 FALSE 반환")
    @Parameter(name = "username", description = "파라미터로 username 입력")
    public ApiResponse<Boolean> checkClientId(@RequestParam(name = "clientId") String clientId) {

        return ApiResponse.onSuccess(memberService.checkClientId(clientId));
    }

    @GetMapping("/isExistNickname")
    @Operation(summary = "nickname 중복체크 API", description = "중복여부 True/False 반환하는 api" +
            "/ 중복이면 TRUE, 중복이 아니면 FALSE 반환")
    @Parameter(name = "nickname", description = "파라미터로 nickname 입력")
    public ApiResponse<Boolean> checkNickname(@RequestParam(name = "nickname") String nickname) {
        return ApiResponse.onSuccess(memberService.checkNickname(nickname));
    }

    @PutMapping("/update/{memberId}")
    @Operation(summary = "회원 정보 update API", description = "회원정보 수정하는 API입니다. 우선은 nickname과 수강 학기만 변경할 수 있도록 " +
            "해놓았습니다.")
    @Parameters({
            @Parameter(name = "nickname"),
            @Parameter(name = "completeTerm")
    })
    public ApiResponse<MemberResponseDTO.UpdateMemberDTO> update(@PathVariable(name = "memberId") Long memberId,
                                                                 @RequestBody @Valid MemberRequestDTO.UpdateMemberDTO request) {
        Member member = memberService.updateMember(memberId, request);
        return ApiResponse.onSuccess(MemberConverter.toUpdateResultDTO(member));
    }

    @PostMapping("/change-major/{memberId}")
    @Operation(summary = "전공 학과를 변경하는 API", description = "전과생을 위한 전공 학과를 변경하는 API 입니다.")
    @Parameters({
            @Parameter(name = "department_name")
    })
    public ApiResponse<Long> changeDepartment(@PathVariable(name = "memberId") Long memberId,
                                              @RequestBody @Valid MemberRequestDTO.ChangeMajorDTO request) {
        return ApiResponse.onSuccess(memberService.changeDepartment(memberId, request));
    }

    @GetMapping("/my-info/{memberId}")
    @Operation(summary = "나의 정보를 가져오는 API")
    public ApiResponse<MemberResponseDTO.MyInfoDTO> myInfo(@PathVariable(name = "memberId") Long memberId) {
        Member member = memberService.findMember(memberId);
        return ApiResponse.onSuccess(MemberConverter.myInfoDTO(member));
    }

    @DeleteMapping("/delete/{memberId}")
    @Operation(summary = "회원 삭제")
    public ApiResponse<Long> delete(@PathVariable(name = "memberId") Long memberId){
        return ApiResponse.onSuccess(memberService.deleteMember(memberId));
    }
}
