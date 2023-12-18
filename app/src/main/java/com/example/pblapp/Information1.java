package com.example.pblapp;

public class Information1 {
    private String nameOfMember;
    private String loanAmount;
    private  String loanDate;

    public Information1() {
    }
    public Information1(String nameOfMember, String loanAmount, String loanDate) {
        this.nameOfMember = nameOfMember;
        this.loanAmount = loanAmount;
        this.loanDate = loanDate;
    }

    public String getNameOfMember() {
        return nameOfMember;
    }

    public void setNameOfMember(String nameOfMember) {
        this.nameOfMember = nameOfMember;
    }

    public String getloanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getloanDate() {
        return loanDate;
    }

    public void setLoanDate(String collected_date) {
        this.loanDate = loanDate;
    }

}