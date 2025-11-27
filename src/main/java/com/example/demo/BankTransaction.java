package com.example.demo;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bank_transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;        // the primary affected account
    private String type;           // DEPOSIT, WITHDRAW, TRANSFER_DEBIT, TRANSFER_CREDIT
    private Double amount;
    private Instant createdAt;
    private String note;

    public BankTransaction() {}

    public BankTransaction(Long accountId, String type, Double amount, String note) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.createdAt = Instant.now();
    }

    // getters/setters
    public Long getId() { return id; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
