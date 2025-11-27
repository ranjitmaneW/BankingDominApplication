package com.example.demo;

import jakarta.transaction.Transactional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionTimedOutException;

import java.util.List;

@Service
public class BankService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankTransactionRepository txRepository;

    // Create account
    public Account createAccount(String holderName, Double initialBalance) {
        if (initialBalance == null) initialBalance = 0.0;
        Account a = new Account(holderName, initialBalance);
        Account saved = accountRepository.save(a);
        // record initial deposit if > 0
        if (initialBalance > 0) {
            txRepository.save(new BankTransaction(saved.getId(), "DEPOSIT", initialBalance, "Initial deposit"));
        }
        return saved;
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    public List<Account> listAccounts() {
        return accountRepository.findAll();
    }

    public Account updateHolderName(Long id, String newName) {
        Account acc = getAccount(id);
        acc.setHolderName(newName);
        return accountRepository.save(acc);
    }

    public void deleteAccount(Long id) {
        Account acc = getAccount(id);
        accountRepository.delete(acc);
    }

    // deposit (non-transactional minimal)
    @Transactional
    public Account deposit(Long accountId, Double amount) throws BadRequestException {
        if (amount == null || amount <= 0) throw new BadRequestException("Amount must be > 0");
        Account acc = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        acc.setBalance(acc.getBalance() + amount);
        Account saved = accountRepository.save(acc);
        txRepository.save(new BankTransaction(accountId, "DEPOSIT", amount, "Deposit"));
        return saved;
    }

    // withdraw (transactional)
    @Transactional
    public Account withdraw(Long accountId, Double amount) throws BadRequestException {
        if (amount == null || amount <= 0) throw new BadRequestException("Amount must be > 0");
        Account acc = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (acc.getBalance() < amount) throw new InsufficientFundsException("Insufficient balance");
        acc.setBalance(acc.getBalance() - amount);
        Account saved = accountRepository.save(acc);
        txRepository.save(new BankTransaction(accountId, "WITHDRAW", amount, "Withdraw"));
        return saved;
    }

    // Transfer money: atomic operation — debit + credit + two transaction records
    @Transactional
    public String transfer(Long fromAccountId, Long toAccountId, Double amount) throws BadRequestException {
        if (fromAccountId.equals(toAccountId)) throw new BadRequestException("Cannot transfer to same account");
        if (amount == null || amount <= 0) throw new BadRequestException("Amount must be > 0");

        Account from = accountRepository.findById(fromAccountId).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        Account to = accountRepository.findById(toAccountId).orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        if (from.getBalance() < amount) throw new InsufficientFundsException("Insufficient balance in sender account");

        // debit
        from.setBalance(from.getBalance() - amount);
        accountRepository.save(from);
        txRepository.save(new BankTransaction(from.getId(), "TRANSFER_DEBIT", amount, "Transfer to account " + to.getId()));

        // potential failure point -> if something here throws, whole method will rollback
        // credit
        to.setBalance(to.getBalance() + amount);
        accountRepository.save(to);
        txRepository.save(new BankTransaction(to.getId(), "TRANSFER_CREDIT", amount, "Transfer from account " + from.getId()));

        return "Transfer successful";
    }

    // get transaction history
    public List<BankTransaction> getTransactionsForAccount(Long accountId) {
        getAccount(accountId); // ensure exists otherwise ResourceNotFound
        return txRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }
}
