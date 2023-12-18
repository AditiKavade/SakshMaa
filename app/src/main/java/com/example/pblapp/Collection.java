package com.example.pblapp;

public class Collection {
    public String nameOfMember;
    public String collected_date;
    public String collected_amount;
    public String collected_loanamount;
    public String collected_loanfine;

    public Collection(String nameOfMember, String collected_date, String collected_amount, String collected_loanamount, String collected_loanfine)
    {
        this.nameOfMember = nameOfMember;
        this.collected_date = collected_date;
        this.collected_amount = collected_amount;
        this.collected_loanamount = collected_loanamount;
        this.collected_loanfine = collected_loanfine;
    }
}
