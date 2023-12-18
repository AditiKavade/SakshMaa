package com.example.pblapp;

public class Member
{
    public String GroupCode;
    public String memberID;
    public String memberName;
    public String memberMobileNumber;
    public String memberAadhaar;
    public String memberPassword;
    public int memberCollectedAmount;
    public double takenLoan;
    public String isSecretary;
    public String isAdmin;

    public Member(String group_code, String memberID, String add_user, String user_mob, String user_aadhaar, String user_password, int collected_amount, double taken_loan, String isSecretary, String isAdmin)
    {
        GroupCode = group_code;
        this.memberID = memberID;
        memberName = add_user;
        memberMobileNumber = user_mob;
        memberAadhaar = user_aadhaar;
        memberPassword = user_password;
        memberCollectedAmount = collected_amount;
        takenLoan = taken_loan;
        this.isSecretary = isSecretary;
        this.isAdmin = isAdmin;
    }
}
