package com.hsebank.finance.repository;

import com.hsebank.finance.domain.model.Category;

import java.util.*;

/**
 * In-memory реализация репозитория для категорий.
 * Используется для разработки и тестирования без подключения к БД.
 */
public class InMemoryCategoryRepository implements Repository<Category, String> {

    private final Map<String, Category> storage = new HashMap<>();

    @Override
    public void save(Category entity) {
        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<Category> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
