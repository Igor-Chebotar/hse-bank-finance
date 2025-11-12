package com.hsebank.finance.repository;

import com.hsebank.finance.domain.model.Operation;

import java.util.*;

/**
 * In-memory реализация репозитория для операций.
 * Используется без подключения к внешним хранилищам.
 */
public class InMemoryOperationRepository implements Repository<Operation, String> {

    private final Map<String, Operation> storage = new HashMap<>();

    @Override
    public void save(Operation entity) {
        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<Operation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
