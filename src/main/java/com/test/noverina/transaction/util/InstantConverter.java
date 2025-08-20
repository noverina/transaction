package com.test.noverina.transaction.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class InstantConverter implements AttributeConverter<Instant, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;
    @Override
    public String convertToDatabaseColumn(Instant input) {
        return input != null ? FORMATTER.format(input) : null;
    }
    @Override
    public Instant convertToEntityAttribute(String input) {
        return input != null ? Instant.parse(input) : null;
    }
}
