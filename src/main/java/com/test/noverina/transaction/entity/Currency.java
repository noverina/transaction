package com.test.noverina.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Currency extends Auditable {
    @Id
    private String currencyId;
    private String name;
}
