package com.hsebank.finance.repository;

import com.hsebank.finance.domain.model.BankAccount;

import java.util.*;

/**
 * Реализация репозитория для BankAccount, использующая хранение в памяти.
 * Используется на этапе разработки или в тестовой среде.
 */
public class InMemoryBankAccountRepository implements Repository<BankAccount, String> {

    private final Map<String, BankAccount> storage = new HashMap<>();

    @Override
    public void save(BankAccount entity) {
        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<BankAccount> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
