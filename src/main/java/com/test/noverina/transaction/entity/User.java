package com.test.noverina.transaction.entity;

import com.test.noverina.transaction.annotation.UuidV6;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends Auditable {
    @Id
    @UuidV6
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
}
