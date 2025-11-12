package com.hsebank.finance.domain.model;

import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Доменная модель категории доходов/расходов
 */
public class Category {
    private String id;
    private OperationType type;
    private String name;

    public Category() {}
    public Category(String id, OperationType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + getId() + '\'' +
                ", type='" + getType() +
                ", name=" + getName() + '\'' +
                '}';
    }

    /**
     * Принимает visitor для экспорта данных (паттерн Visitor)
     */
    public void accept(DataExportVisitor visitor) {
        visitor.visit(this);
    }
}