package com.example.cardiacrecorder;

public class User {
    public String accountno,name,monbileno,email,pass,gender,dateofbirth;
    boolean verified;

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonbileno() {
        return monbileno;
    }

    public void setMonbileno(String monbileno) {
        this.monbileno = monbileno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public User(){}

    public User(String accountno, String name, String monbileno, String email, String pass, String gender, String dateofbirth, boolean verified) {
        this.accountno = accountno;
        this.name = name;
        this.monbileno = monbileno;
        this.email = email;
        this.pass = pass;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.verified = verified;
    }
}
