package com.example.quicksells.common.util;

import com.example.quicksells.common.annotation.Adult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AdultValidator implements ConstraintValidator<Adult, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        try {
            LocalDate birthDate = LocalDate.parse(value, FORMATTER);
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            return age >= 19;

        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
