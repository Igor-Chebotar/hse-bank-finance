package com.hsebank.finance.factory;

import com.hsebank.finance.domain.model.BankAccount;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Фабрика для создания банковских счетов с валидацией
 * Гарантирует корректность данных и автоматическую генерацию ID
 */
public class BankAccountFactory {
    public BankAccount create(String name, BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Баланс не может быть null");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя банковского счёта не может быть пустым");
        }

        String id = UUID.randomUUID().toString();
        BankAccount bankAccount = new BankAccount();

        bankAccount.setId(id);
        bankAccount.setBalance(balance);
        bankAccount.setName(name);

        return bankAccount;
    }
}
