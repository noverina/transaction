package com.test.noverina.transaction.entity;

import com.test.noverina.transaction.annotation.UuidV6;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Currency extends Auditable {
    @Id
    @UuidV6
    private String currencyId;
    private String name;
}
