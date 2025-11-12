package com.hsebank.finance.factory;

import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.OperationType;

import java.util.UUID;

/**
 * Фабрика для создания категорий с валидацией
 * Гарантирует корректность данных и автоматическую генерацию ID
 */
public class CategoryFactory {
    public Category create(OperationType type, String name) {
        if (type == null) {
            throw new IllegalArgumentException("Тип категории не может быть null");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        String id = UUID.randomUUID().toString();
        Category category = new Category();

        category.setId(id);
        category.setName(name);
        category.setType(type);

        return category;
    }
}
