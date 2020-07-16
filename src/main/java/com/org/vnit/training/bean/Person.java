package com.org.vnit.training.bean;

import com.org.vnit.training.dao.Server;
import com.org.vnit.training.dao.Transaction;

import java.util.List;

public class Person {
    private Integer mobileNumber, balance;
    String name;

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public Person(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Person() {
    }

    public void addMoney(Integer amount, Server server) {
        server.addMoney(this.mobileNumber, amount);
    }

    public void withdrawMoney(Integer amount, Server server) {
        server.withdrawMoney(this.mobileNumber, amount);
    }

    public Integer viewBalance(Server server) {
        return server.getBalance(this.mobileNumber);
    }

    public List<Transaction> transactionHistory(Server server) {
        return server.transactionHistory(mobileNumber);
    }

    public void changeName(String name, Server server) {
        server.changeName(this.mobileNumber, name);
    }

    public void viewDetails(Integer mobileNumber, Server server) {
        server.viewDetails(mobileNumber);
    }
}
