package com.example.pblapp;

public class Information2 {
    private String nameOfMember;
    private String collected_amount;
    private  String collected_date;


    public Information2() {
    }

    public Information2(String nameOfMember, String collected_amount, String collected_date) {
        this.nameOfMember = nameOfMember;
        this.collected_amount = collected_amount;
        this.collected_date = collected_date;
    }

    public String getNameOfMember() {
        return nameOfMember;
    }

    public void setNameOfMember(String nameOfMember) {
        this.nameOfMember = nameOfMember;
    }

    public String getCollected_amount() {
        return collected_amount;
    }

    public void setCollected_amount(String collected_amount) {
        this.collected_amount = collected_amount;
    }

    public String getCollected_date() {
        return collected_date;
    }

    public void setCollected_date(String collected_date) {
        this.collected_date = collected_date;
    }
}
