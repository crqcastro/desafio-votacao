package br.com.cesarcastro.votacao.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
public class DateGreaterThanValidator implements ConstraintValidator<DateGreaterThan, Object> {

    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(DateGreaterThan constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startDateField = value.getClass().getDeclaredField(this.startDateField);
            Field endDateField = value.getClass().getDeclaredField(this.endDateField);
            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            LocalDateTime startDate = (LocalDateTime) startDateField.get(value);
            LocalDateTime endDate = (LocalDateTime) endDateField.get(value);

            if (startDate == null || endDate == null) {
                return true;
            }

            return endDate.isAfter(startDate);
        } catch (Exception e) {
            log.error("Erro ao validar a data: {}", e.getMessage());
            return false;
        }
    }
}
