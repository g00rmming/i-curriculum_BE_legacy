package goorming.iCurriculum.common.code.status;

import goorming.iCurriculum.common.code.BaseErrorCode;
import goorming.iCurriculum.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "로그인 인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //login
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "LOGIN4001", "로그인 실패"),
    LOGIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "LOGIN4002", "로그인 정보가 존재하지 않습니다."),
    //token
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOKEN4001", "토큰이 존재하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "TOKEN4002", "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "TOKEN4003", "토큰이 유효하지 않습니다."),

    //member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 존재하지 않습니다."),
    //수강
    TAKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TAKE4001", "수강내역이 존재하지 않습니다."),
    TAKE_DUPLICATED(HttpStatus.BAD_REQUEST, "TAKE4002", "중복된 수강내역 입니다."),
    TAKE_BINDING_FAIL(HttpStatus.BAD_REQUEST, "TAKE4003", "파라미터 바인딩 실패"),

    //수업
    COURSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "COURSE4001", "수업이 존재하지 않습니다."),
    COURSE_BINDING_FAIL(HttpStatus.BAD_REQUEST, "COURSE4002","파라미터 바인딩 실패")


    ;



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(httpStatus)
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }
}
