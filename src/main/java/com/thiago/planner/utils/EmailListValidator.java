package com.thiago.planner.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EmailListValidator implements ConstraintValidator<ValidEmailList, List<String>> {

    @Override
    public void initialize(ValidEmailList constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

            for (String email : value) {
                if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    return false;
                }
            }

            return true;

        }

    }