package com.hsebank.finance.exporter;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;

/**
 * Интерфейс visitor для экспорта данных в различные форматы
 * Паттерн Visitor позволяет добавлять новые форматы экспорта
 * без изменения доменных классов
 */
public interface DataExportVisitor {
    void visit(BankAccount account);
    void visit(Category category);
    void visit(Operation operation);
}