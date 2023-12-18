package com.example.pblapp;
public class Group {
    public String group_name;
    public String admin_name;
    public String mobile_no;
    public String start_year;
    public String start_month;
    public String monthly_saving;
    public String group_code;
    public String admin_password;
    public int total_amount;
    public int total_given_loan;
    public Group(String group_name, String admin_name, String mobile_no, String start_year, String start_month, String monthly_saving, String group_code, String admin_password, int total_amount, int total_given_loan)
    {
        this.group_name = group_name;
        this.admin_name = admin_name;
        this.mobile_no = mobile_no;
        this.start_year = start_year;
        this.start_month = start_month;
        this.monthly_saving = monthly_saving;
        this.group_code = group_code;
        this.admin_password = admin_password;
        this.total_amount = total_amount;
        this.total_given_loan = total_given_loan;
    }
}

