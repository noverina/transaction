package com.test.noverina.transaction.repository;

import com.test.noverina.transaction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
