package com.test.noverina.transaction.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

@Converter(autoApply = false)
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {
    @Override
    public String convertToDatabaseColumn(MonetaryAmount amount) {
        return amount != null ?
                amount.getCurrency().getCurrencyCode() + " " + amount.getNumber().toString()
                : null;
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        if (dbData == null || !dbData.contains(" ")) return null;
        String[] split = dbData.trim().split(" ");
        if (split.length != 2) {
            return null;
        }
        String code = split[0];
        BigDecimal amount = new BigDecimal(split[1]);
        return Monetary.getDefaultAmountFactory()
                .setCurrency(code)
                .setNumber(amount)
                .create();
    }
}
