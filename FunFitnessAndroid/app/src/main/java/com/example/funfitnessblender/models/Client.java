package com.example.funfitnessblender.models;



import java.time.LocalDate;

public class Client { //d
    private String id;
    private String firstName;
    private String lastName;
    private String date;
    private String age;
    private String email;
    private String address;
    private String parentFirstName;
    private String birthDate;
    private String phone;

    private String amount;
    private String status;
    private String program;
    private String area;
    private String interest;
    private String referral;
    private String referralName;

    // No-argument constructor
    public Client() { // Renamed from Enquiry() to Client()
    }

    public Client(String id, String firstName, String lastName, String date, String age, String email, String address, String parentFirstName, String birthDate, String phone, String amount, String status, String program, String area, String interest, String referral, String referralName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.age = age;
        this.email = email;
        this.address = address;
        this.parentFirstName = parentFirstName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.amount = amount;
        this.status = status;
        this.program = program;
        this.area = area;
        this.interest = interest;
        this.referral = referral;
        this.referralName = referralName;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDate() {
        return date;
    }


    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getParentFirstName() {
        return parentFirstName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getProgram() {
        return program;
    }

    public String getArea() {
        return area;
    }

    public String getInterest() {
        return interest;
    }

    public String getReferral() {
        return referral;
    }

    public String getReferralName() {
        return referralName;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParentFirstName(String parentFirstName) {
        this.parentFirstName = parentFirstName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public void setReferralName(String referralName) {
        this.referralName = referralName;
    }

    public String getClientId() {
        return id;
    }
}

