package com.sample.account_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    public void initialize(Password constraintAnnotation) {
        // Nothing to do here
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        // TODO: validate password
        //  - at least 8 characters
        //  - at least 1 uppercase letter
        //  - at least 1 lowercase letter
        //  - at least 1 number
        //  - at least 1 special character

        return value.matches(PASSWORD_PATTERN);
    }

}