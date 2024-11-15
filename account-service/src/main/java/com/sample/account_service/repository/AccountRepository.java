package com.sample.account_service.repository;

import com.sample.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByName(String name);

    @Query("SELECT acc FROM Account acc WHERE acc.name LIKE %:name%")
//    @Query(nativeQuery = true, value = "SELECT * FROM accounts WHERE name LIKE %:name%")
    List<Account> findByNameLike(String name);
}
