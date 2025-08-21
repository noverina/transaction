package com.test.noverina.transaction.repository;

import com.test.noverina.transaction.dto.TransactionListDto;
import com.test.noverina.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query(value = """
        SELECT new com.test.noverina.transaction.dto.TransactionListDto(t.transactionId, t.date, t.type, t.amount, a.iban, c.currencyId, t.description)
        FROM Transaction t
        JOIN t.account a
        JOIN a.currency c
        WHERE a.accountId = :accountId AND t.date > :start AND t.date < :end
        """, countQuery = """
        SELECT COUNT(t)
        FROM Transaction t
        JOIN t.account a
        JOIN a.currency c
        WHERE a.accountId = :accountId AND t.date > :start AND t.date < :end
        """
    )
    public Page<TransactionListDto> findAllByAccountWithinAMonth(@Param("accountId")String accountId, @Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end, Pageable pageable);
}
