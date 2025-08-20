package com.test.noverina.transaction.entity;

import com.test.noverina.transaction.util.InstantConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class Auditable {
    @CreatedBy
    protected String createdBy;
    @CreatedDate
    @Convert(converter = InstantConverter.class)
    protected Instant createdAt;
    @LastModifiedBy
    protected String updatedBy;
    @LastModifiedDate
    @Convert(converter = InstantConverter.class)
    protected Instant updatedAt;
}
