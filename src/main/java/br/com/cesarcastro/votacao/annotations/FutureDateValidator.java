package br.com.cesarcastro.votacao.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDate> {

    @Override
    public void initialize(FutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(LocalDate.now());
    }
}
