// src/main/java/com/example/demo/Account.java
package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BASIC
    private String holderName;          // full name
    private Double balance = 0.0;       // existing

    // ACCOUNT INFO
    private String accountType;         // e.g. SAVINGS, CURRENT, SALARY

    // PERSONAL INFO
    private String mobileNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;              // optional: MALE/FEMALE/OTHER

    // KYC / ID
    private String aadhaarNumber;
    private String panNumber;

    // ADDRESS
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    // ADDRESS PROOF
    private String addressProofType;    // e.g. AADHAAR, VOTER_ID, PASSPORT
    private String addressProofNumber;  // document number
    private String kycDocPath;          // file path / URL of uploaded KYC docs

    // 🆕 CUSTOMER PHOTO
    private String photoPath;           // file path / URL of customer photo

    // NOMINEE
    private String nomineeName;
    private String nomineeRelation;

    // OTHER
    private String education;           // SSC / HSC / GRADUATE / POSTGRAD
    private String occupation;          // job / business, etc.

    // NEW: Role field
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;    // default USER

    public Account() {}

    public Account(Long id, String holderName, Double balance, String accountType, String mobileNumber, String email,
                   LocalDate dateOfBirth, String gender, String aadhaarNumber, String panNumber, String addressLine1,
                   String addressLine2, String city, String state, String postalCode, String country, String addressProofType,
                   String addressProofNumber, String kycDocPath, String photoPath, String nomineeName,
                   String nomineeRelation, String education, String occupation, Role role) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
        this.accountType = accountType;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.aadhaarNumber = aadhaarNumber;
        this.panNumber = panNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.addressProofType = addressProofType;
        this.addressProofNumber = addressProofNumber;
        this.kycDocPath = kycDocPath;
        this.photoPath = photoPath;
        this.nomineeName = nomineeName;
        this.nomineeRelation = nomineeRelation;
        this.education = education;
        this.occupation = occupation;
        this.role = role;
    }

    // ===== GETTERS / SETTERS =====

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getHolderName() { return holderName; }

    public void setHolderName(String holderName) { this.holderName = holderName; }

    public Double getBalance() { return balance; }

    public void setBalance(Double balance) { this.balance = balance; }

    public String getAccountType() { return accountType; }

    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getMobileNumber() { return mobileNumber; }

    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getAadhaarNumber() { return aadhaarNumber; }

    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }

    public String getPanNumber() { return panNumber; }

    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getAddressLine1() { return addressLine1; }

    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getAddressLine2() { return addressLine2; }

    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getAddressProofType() { return addressProofType; }

    public void setAddressProofType(String addressProofType) { this.addressProofType = addressProofType; }

    public String getAddressProofNumber() { return addressProofNumber; }

    public void setAddressProofNumber(String addressProofNumber) { this.addressProofNumber = addressProofNumber; }

    public String getKycDocPath() { return kycDocPath; }

    public void setKycDocPath(String kycDocPath) { this.kycDocPath = kycDocPath; }

    public String getPhotoPath() { return photoPath; }

    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getNomineeName() { return nomineeName; }

    public void setNomineeName(String nomineeName) { this.nomineeName = nomineeName; }

    public String getNomineeRelation() { return nomineeRelation; }

    public void setNomineeRelation(String nomineeRelation) { this.nomineeRelation = nomineeRelation; }

    public String getEducation() { return education; }

    public void setEducation(String education) { this.education = education; }

    public String getOccupation() { return occupation; }

    public void setOccupation(String occupation) { this.occupation = occupation; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
