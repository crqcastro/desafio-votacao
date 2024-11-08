package br.com.cesarcastro.votacao.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDateTime> {

    @Override
    public void initialize(FutureDate constraintAnnotation) {  // Noncompliant - method is empty
    }

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
        log.info("Validating date: {}", date);
        return date.isAfter(LocalDateTime.now());
    }
}
