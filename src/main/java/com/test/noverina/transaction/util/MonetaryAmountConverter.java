package com.test.noverina.transaction.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    @Override
    public String convertToDatabaseColumn(MonetaryAmount amount) {
        return amount != null ?
                amount.getCurrency().getCurrencyCode() + " " + amount.getNumber().toString()
                : null;
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String[] split = dbData.trim().split(" ");
        String code = split[0];
        BigDecimal amount = new BigDecimal(split[1]);
        return Monetary.getDefaultAmountFactory()
                .setCurrency(code)
                .setNumber(amount)
                .create();
    }
}
