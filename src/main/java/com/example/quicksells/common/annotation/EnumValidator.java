package com.example.quicksells.common.annotation;

import com.example.quicksells.common.util.EnumValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

// Enum 커스텀 어노테이션 _ Valid 유효성 검사
@Documented
@Constraint(validatedBy = EnumValidatorImpl.class) // 실제 검증 로직을 수행할 클래스 지정
@Target(ElementType.FIELD) // 필드에 적용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
@NotNull(message = "필수 값입니다.")
@ReportAsSingleViolation // 커스텀 어노테이션 자체에 정의된 하나의 오류 메시지만 반환하도록 강제함
public @interface EnumValidator {

    // 필수로 검증할 Enum 클래스 타입
    Class<? extends Enum<?>> targetEnum();

    // 유효성 검증 실패시 기본 메시지
    String message() default "유효하지 않은 Enum 상수입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

