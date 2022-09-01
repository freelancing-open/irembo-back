package com.example.iremboback.service.Implementation;

import com.example.iremboback.model.AccountVerification;
import com.example.iremboback.model.Account_Status;
import com.example.iremboback.model.Users;
import com.example.iremboback.repository.AccountVerificationRepository;
import com.example.iremboback.service.AccountVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountVerificationImpl implements AccountVerificationService {
    
    @Autowired
    private AccountVerificationRepository verificationRepository;
    
    @Override
    public Optional<AccountVerification> initVerification(Users u) {
        return Optional.of(verificationRepository.save(new AccountVerification("", "", Account_Status.UNVERIFIED, u)));
    }

    @Override
    public Optional<AccountVerification> initVerificationAdmin(Users u) {
        return Optional.of(verificationRepository.save(new AccountVerification("rw-001", "Images/Admin.png", Account_Status.VERIFIED, u)));
    }

    @Override
    public Optional<AccountVerification> verify(String id, String docLink, Users u) {
        Optional<AccountVerification> getAccount = verificationRepository.findAll().stream().filter(accv -> accv.getUsers().getEmail().equals(u.getEmail())).findFirst();
        if(getAccount.isEmpty()){
            AccountVerification create = new AccountVerification(id, docLink, Account_Status.VERIFIED, u);
            return Optional.of(verificationRepository.save(create));
        }else {
            //Update Doc
           getAccount.get().setIdentification(id);
           getAccount.get().setDocumentLink(docLink);
           getAccount.get().setStatus(Account_Status.VERIFIED);
           return getAccount;
        }
    }
}
