package com.org.vnit.training.dao;

import java.time.LocalDateTime;

public class Transaction {
    String details;
    Integer balance;
    LocalDateTime dateTime;

    public Integer getBalance() {
        return balance;
    }

    public String getDetails() {
        return details;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
