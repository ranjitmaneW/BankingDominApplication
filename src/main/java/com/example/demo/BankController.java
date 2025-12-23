package com.example.demo;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank")
@CrossOrigin(origins = "http://localhost:4200")
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private AccountRepository accountRepository;

 // src/main/java/com/example/demo/BankController.java

    @PostMapping(
            value = "/open-account",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<Account> openAccount(
            @RequestPart("account") Account account,
            @RequestPart("aadhaarFile") MultipartFile aadhaarFile,
            @RequestPart("panFile") MultipartFile panFile,
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile   // 🆕
    ) throws IOException {

        // Base folders
        String kycDir = "src/main/resources/static/uploads/kyc/";
        String photoDir = "src/main/resources/static/uploads/photos/";

        Files.createDirectories(Paths.get(kycDir));
        Files.createDirectories(Paths.get(photoDir));

        // ---- Save Aadhaar ----
        String aadhaarName = System.currentTimeMillis() + "_AADHAAR_" + aadhaarFile.getOriginalFilename();
        Path aadhaarPath = Paths.get(kycDir + aadhaarName);
        Files.write(aadhaarPath, aadhaarFile.getBytes());

        // ---- Save PAN ----
        String panName = System.currentTimeMillis() + "_PAN_" + panFile.getOriginalFilename();
        Path panPath = Paths.get(kycDir + panName);
        Files.write(panPath, panFile.getBytes());

        // ✅ Web paths for KYC
        String aadhaarWebPath = "/uploads/kyc/" + aadhaarName;
        String panWebPath     = "/uploads/kyc/" + panName;
        account.setKycDocPath(aadhaarWebPath + ";" + panWebPath);

        // 🆕 Save customer photo (if provided)
        if (photoFile != null && !photoFile.isEmpty()) {
            String photoName = System.currentTimeMillis() + "_PHOTO_" + photoFile.getOriginalFilename();
            Path photoPath = Paths.get(photoDir + photoName);
            Files.write(photoPath, photoFile.getBytes());

            String photoWebPath = "/uploads/photos/" + photoName;
            account.setPhotoPath(photoWebPath);
        }

        Account saved = bankService.createAccount(account);
        return ResponseEntity.ok(saved);
    }
    // ====================== LOGIN ======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username"); // Full name (holderName)
        String password = body.get("password"); // Email

        if (username == null || password == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Username (name) and password (email) required");
        }

        // Search from Account table by holderName + email
        Optional<Account> opt = accountRepository.findByHolderNameAndEmail(username, password);

        if (opt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        Account acc = opt.get();

        Map<String, Object> resp = new HashMap<>();
        resp.put("username", acc.getHolderName());
        resp.put("role", acc.getRole().name());   // ADMIN / USER
        resp.put("accountId", acc.getId());

        return ResponseEntity.ok(resp);
    }

    // ====================== ACCOUNT QUERIES ======================
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> listAccounts() {
        return ResponseEntity.ok(bankService.listAccounts());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bankService.getAccount(id));
    }

    @PutMapping("/accounts/{id}/name")
    public ResponseEntity<Account> updateName(@PathVariable("id") Long id,
                                              @RequestParam("name") String name) {
        return ResponseEntity.ok(bankService.updateHolderName(id, name));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        bankService.deleteAccount(id);
        return ResponseEntity.ok("Deleted");
    }

    // ✅ NEW: update entire personal info (used by Admin "Edit Personal Info")
    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @RequestBody Account updatedAccount) {

        // Make sure the path ID is the one we update
        updatedAccount.setId(id);
        Account saved = bankService.updateAccount(id, updatedAccount);
        return ResponseEntity.ok(saved);
    }

    // ====================== MONEY OPERATIONS ======================
    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable("id") Long id,
            @RequestParam("amount") Double amount) throws BadRequestException {

        return ResponseEntity.ok(bankService.deposit(id, amount));
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable("id") Long id,
            @RequestParam("amount") Double amount) throws BadRequestException {

        return ResponseEntity.ok(bankService.withdraw(id, amount));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam("from") Long from,
                                           @RequestParam("to") Long to,
                                           @RequestParam("amount") Double amount) throws BadRequestException {
        return ResponseEntity.ok(bankService.transfer(from, to, amount));
    }

    // ====================== TRANSACTIONS ======================
    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<BankTransaction>> transactions(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bankService.getTransactionsForAccount(id));
    }
}
