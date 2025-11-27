package com.example.demo;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    // create account
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> body) {

        String holderName = (String) body.get("holderName");
        Double initialBalance = body.get("initialBalance") != null 
                ? Double.valueOf(body.get("initialBalance").toString()) 
                : 0.0;

        Account a = bankService.createAccount(holderName, initialBalance);
        return ResponseEntity.ok(a);
    }


    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> listAccounts() {
        return ResponseEntity.ok(bankService.listAccounts());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable ("id")Long id) {
        return ResponseEntity.ok(bankService.getAccount(id));
    }

    @PutMapping("/accounts/{id}/name")
    public ResponseEntity<Account> updateName(@PathVariable  ("id") Long id, @RequestParam ("name")String name) {
        return ResponseEntity.ok(bankService.updateHolderName(id, name));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        bankService.deleteAccount(id);
        return ResponseEntity.ok("Deleted");
    }
    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable("id") Long id,
            @RequestParam("amount") Double amount) throws BadRequestException {

        return ResponseEntity.ok(bankService.deposit(id, amount));
    }

    // withdraw
    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable("id") Long id, @RequestParam  ("amount")  Double amount) throws BadRequestException {
        return ResponseEntity.ok(bankService.withdraw(id, amount));
    }

    // transfer
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam ("from")Long from,
                                           @RequestParam ("to")Long to,
                                           @RequestParam    ("amount")Double amount) throws BadRequestException {
        return ResponseEntity.ok(bankService.transfer(from, to, amount));
    }

    // transaction history
    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<BankTransaction>> transactions(@PathVariable ("id")Long id) {
        return ResponseEntity.ok(bankService.getTransactionsForAccount(id));
    }
}
