package br.com.cesarcastro.votacao.support.exceptions.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record FieldErrorDto(String fieldName, String reason) implements Serializable, Comparable<FieldErrorDto> {

    @Override
    public int compareTo(@NotNull FieldErrorDto o) {
        return fieldName.compareTo(o.fieldName);
    }
}