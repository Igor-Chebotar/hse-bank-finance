package com.hsebank.finance.factory;

import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.domain.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Фабрика для создания финансовых операций с валидацией
 * Критично: запрещает создание операций с отрицательной или нулевой суммой
 */
public class OperationFactory {
    public Operation create(OperationType type, String bankAccountId,
                            BigDecimal amount, String categoryId) {
        if (amount == null) {
            throw new IllegalArgumentException("Сумма не может быть null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля");
        }

        if (bankAccountId == null || bankAccountId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID банковского счёта не может быть пустым");
        }

        if (categoryId == null || categoryId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID категории не может быть пустым");
        }

        if (type == null) {
            throw new IllegalArgumentException("Тип операции не может быть null");
        }

        String id = UUID.randomUUID().toString();
        Operation operation = new Operation();

        operation.setId(id);
        operation.setType(type);
        operation.setBankAccountId(bankAccountId);
        operation.setAmount(amount);
        operation.setDate(LocalDate.now());
        operation.setCategoryId(categoryId);

        return operation;
    }
}
