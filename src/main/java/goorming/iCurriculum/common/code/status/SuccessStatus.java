package goorming.iCurriculum.common.code.status;

import goorming.iCurriculum.common.code.BaseCode;
import goorming.iCurriculum.common.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {

        return ReasonDTO.builder()
                .code(code)
                .message(message)
                .build();

    }

    @Override
    public ReasonDTO getReasonHttpStatus() {

        return ReasonDTO.builder()
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
