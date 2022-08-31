package com.example.iremboback.repository;

import com.example.iremboback.model.AccountVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountVerificationRepository extends JpaRepository<AccountVerification, Long> {
}
