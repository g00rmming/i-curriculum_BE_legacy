package goorming.iCurriculum.course.entity;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category {
    MAJOR_ESSENTIAL("전공필수"),
    MAJOR_SELECTIVE("전공선택"),
    GENERAL_ESSENTIAL("교양필수"),
    GENERAL_SELECTIVE("교양선택"),
    GENERAL_CORE_ONE("핵심교양 1"),
    GENERAL_CORE_TWO("핵심교양 2"),
    GENERAL_CORE_THREE("핵심교양 3"),
    GENERAL_CORE_FOUR("핵심교양 4"),
    GENERAL_CORE_FIVE("핵심교양 5"),
    GENERAL_CORE_SIX("핵심교양 6"),
    GENERAL_CREATIVE("창의영역")
    ;
    private final String name;

    public static Category getCategoryByName(String name) {
        return Arrays.stream(Category.values())
                .filter(grade -> grade.getName().equals(name))
                .findFirst()
                .orElse(Category.GENERAL_SELECTIVE);
    }
}
