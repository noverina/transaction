package com.test.noverina.transaction.entity;

import com.test.noverina.transaction.annotation.UuidV6;
import com.test.noverina.transaction.enums.AccountStatus;
import com.test.noverina.transaction.util.MonetaryAmountConverter;
import jakarta.persistence.*;
import lombok.*;

import javax.money.MonetaryAmount;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account extends Auditable {
    @Id
    @UuidV6
    private String accountId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    private String iban;
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
