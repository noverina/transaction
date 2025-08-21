package com.test.noverina.transaction.repository;

import com.test.noverina.transaction.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
