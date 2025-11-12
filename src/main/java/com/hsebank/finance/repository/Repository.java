package com.hsebank.finance.repository;

import java.util.List;
import java.util.Optional;

/**
 * Обобщённый интерфейс репозитория для хранения и доступа к сущностям.
 *
 * @param <T> тип сущности
 * @param <ID> тип идентификатора
 */
public interface Repository<T, ID> {

    /**
     * Сохраняет или обновляет сущность.
     */
    void save(T entity);

    /**
     * Возвращает сущность по идентификатору, если найдена.
     */
    Optional<T> findById(ID id);

    /**
     * Возвращает все сохранённые сущности.
     */
    List<T> findAll();

    /**
     * Удаляет сущность по идентификатору.
     */
    void delete(ID id);
}
