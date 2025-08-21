package com.test.noverina.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListViewDto {
    private String transactionId;
    private OffsetDateTime transactionDate;
    private String amount;
    private String iban;
    private String description;
}
