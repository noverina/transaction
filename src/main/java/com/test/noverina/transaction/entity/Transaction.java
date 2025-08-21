package com.test.noverina.transaction.entity;

import com.test.noverina.transaction.enums.TransactionType;
import com.test.noverina.transaction.util.MonetaryAmountConverter;
import com.test.noverina.transaction.util.OffsetDateTimeConverter;
import jakarta.persistence.*;
import lombok.*;

import javax.money.MonetaryAmount;
import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "txn")
public class Transaction extends Auditable {
    @Id
    private String transactionId;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Convert(converter = OffsetDateTimeConverter.class)
    private OffsetDateTime date;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount amount;
    private String description;
}
