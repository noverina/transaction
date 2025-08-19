package com.test.noverina.transaction.entity;

import com.test.noverina.transaction.enums.TransactionStatus;
import com.test.noverina.transaction.enums.TransactionType;
import com.test.noverina.transaction.util.MonetaryAmountConverter;
import com.test.noverina.transaction.util.ZonedDateTimeConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.money.MonetaryAmount;
import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Transaction extends Auditable {
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime date;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String description;
}
