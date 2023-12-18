package com.example.pblapp;

public class ClassLoan {
    public String nameOfMember;
    public String loanDate;
    public String loanAmount;
    public String loanInterest;
    public double returnLoanAmount;

    public ClassLoan(String nameOfMember, String loanDate, String loanAmount, String loanInterest, double returnLoanAmount)
    {
        this.nameOfMember = nameOfMember;
        this.loanInterest = loanInterest;
        this.loanDate = loanDate;
        this.loanAmount = loanAmount;
        this.returnLoanAmount = returnLoanAmount;
    }

}
