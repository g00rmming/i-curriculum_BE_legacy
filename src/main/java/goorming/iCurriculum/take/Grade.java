package goorming.iCurriculum.take;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Grade {
    A_PLUS(4.5),
    A_ZERO(4.0),
    B_PLUS(3.5),
    B_ZERO(3.0),
    C_PLUS(2.5),
    C_ZERO(2.0),
    D_PLUS(1.5),
    D_ZERO(1.0),
    FAIL(0.0),
    PASS(-1.0); //사용시 주의 하세요

    private final Double score;
}
