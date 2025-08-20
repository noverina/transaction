package com.test.noverina.transaction.util;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = false)
public class OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    @Override
    public String convertToDatabaseColumn(OffsetDateTime input) {
        return input != null ? input.format(FORMATTER) : null;
    }
    @Override
    public OffsetDateTime convertToEntityAttribute(String input) {
        return input != null ? OffsetDateTime.parse(input) : null;
    }
}
