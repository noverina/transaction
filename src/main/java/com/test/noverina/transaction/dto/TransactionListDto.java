package com.test.noverina.transaction.dto;

import com.test.noverina.transaction.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.money.MonetaryAmount;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListDto {
    private String transactionId;
    private OffsetDateTime transactionDate;
    private TransactionType type;
    private MonetaryAmount amount;
    private String iban;
    private String accountCurrency;
    private String description;
}
