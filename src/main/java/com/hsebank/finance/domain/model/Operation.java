package com.hsebank.finance.domain.model;

import com.hsebank.finance.exporter.DataExportVisitor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Доменная модель финансовой операции (дохода или расхода)
 */
public class Operation {
    private String id;
    private OperationType type;
    private String bankAccountId;
    private BigDecimal amount;
    private LocalDate date;
    private String categoryId;
    private String description;

    public Operation() {}
    public Operation(String id,
                     OperationType type,
                     String bankAccountId,
                     BigDecimal amount,
                     LocalDate date,
                     String categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }
    public Operation(String id,
                     OperationType type,
                     String bankAccountId,
                     BigDecimal amount,
                     LocalDate date,
                     String categoryId,
                     String description) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public OperationType getType() {
        return this.type;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountId() {
        return this.bankAccountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description != null ? description : "Без описания";
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id='" + getId() + '\'' +
                ", type=" + getType() +
                ", bankAccountId='" + getBankAccountId() + '\'' +
                ", amount=" + getAmount() +
                ", date=" + getDate() +
                ", categoryId='" + getCategoryId() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }

    /**
     * Принимает visitor для экспорта данных (паттерн Visitor)
     */
    public void accept(DataExportVisitor visitor) {
        visitor.visit(this);
    }
}