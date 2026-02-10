package ru.zyryanova.LedgerService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.LedgerService.error.NonRetryableException;
import ru.zyryanova.LedgerService.repository.AccountRepo;

@Service
public class AccountService {
    private final AccountRepo accountRepo;

    @Autowired
    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Transactional
    public void deltaAmount(String senderId, String recipientId, long amount){
        if(amount<=0){
            throw new NonRetryableException("Amount must be >0");
        }
        if (senderId.equals(recipientId)) throw new NonRetryableException("Sender and recipient must differ");

        int updated = accountRepo.deltaAmount(senderId,recipientId,amount);
        if(updated!=2){
            throw new NonRetryableException("Invalid operation (not found or insufficient funds)");
        }
    }




}
