package com.test.noverina.transaction.repository;

import com.test.noverina.transaction.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
