package com.test.noverina.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.noverina.transaction.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class TransactionMessageDto {
    private String currencyId;
    private String accountId;
    private String date;
    private TransactionType type;
    private String amount;
    private String description;
}
