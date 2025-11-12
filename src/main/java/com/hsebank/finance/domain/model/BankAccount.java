package com.hsebank.finance.domain.model;

import com.hsebank.finance.exporter.DataExportVisitor;

import java.math.BigDecimal;

/**
 * Доменная модель банковского счёта
 */
public class BankAccount {
    private String id;
    private String name;
    private BigDecimal balance;

    public BankAccount () {}
    public BankAccount (String id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", balance=" + getBalance() +
                '}';
    }

    /**
     * Принимает visitor для экспорта данных (паттерн Visitor)
     */
    public void accept(DataExportVisitor visitor) {
        visitor.visit(this);
    }
}