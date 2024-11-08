package br.com.cesarcastro.votacao.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

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
            e.printStackTrace();
            return false;
        }
    }
}
