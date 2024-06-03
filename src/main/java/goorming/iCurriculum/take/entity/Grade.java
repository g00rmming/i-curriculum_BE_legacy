package goorming.iCurriculum.take.entity;


import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Grade {
    A_PLUS("A+", 4.5),
    A_ZERO("A0", 4.0),
    B_PLUS("B+", 3.5),
    B_ZERO("B0", 3.0),
    C_PLUS("C+", 2.5),
    C_ZERO("C0", 2.0),
    D_PLUS("D+", 1.5),
    D_ZERO("D0", 1.0),
    FAIL("F", 0.0),
    PASS("P", -1.0); //사용시 주의 하세요

    private final String string;
    private final Double score;

    public static Grade getGrade(String name) {
        return Arrays.stream(Grade.values())
                .filter(grade -> grade.getString().equals(name))
                .findFirst()
                .orElse(Grade.PASS);
    }
}
