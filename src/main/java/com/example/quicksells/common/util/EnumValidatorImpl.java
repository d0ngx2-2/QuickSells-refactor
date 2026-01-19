package com.example.quicksells.common.util;

import com.example.quicksells.common.annotation.EnumValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
    Set<String> valueSet = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 또는 빈 문자열은 허용 (NotNull과 함께 사용)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        // 대소문자 구분 없이 Enum 상수를 비교
        return valueSet.contains(value.toUpperCase());
    }

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueSet = new HashSet<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.targetEnum();

        // Enum 상수들을 Set에 추가
        Enum<?>[] enumConstants = enumClass.getEnumConstants();

        // 어노테이션이 지정한 Enum 클래스에서 모든 멤버 이름을 추출
        if (enumConstants != null) {
            for (Enum<?> enumConstant : enumConstants) {
                valueSet.add(enumConstant.name().toUpperCase());
            }
        }
    }
}
