package com.example.iremboback.service;


import com.example.iremboback.model.AccountVerification;
import com.example.iremboback.model.Users;

import java.util.Optional;

public interface AccountVerificationService {

    Optional<AccountVerification> initVerification(Users users);

    Optional<AccountVerification> initVerificationAdmin(Users users);

    Optional<AccountVerification> verify(String id, String docLink, Users users);
}
