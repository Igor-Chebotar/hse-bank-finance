package com.hsebank.finance.importer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Абстрактный импортёр данных с реализацией паттерна "Шаблонный метод".
 * Общая логика импорта: чтение → парсинг → валидация.
 */
public abstract class DataImporter<T> {

    /**
     * Шаблонный метод: определяет шаги процесса импорта.
     */
    public final List<T> importFromFile(String filePath) throws IOException {
        System.out.println("Чтение файла: " + filePath);
        String content = readFile(filePath);

        System.out.println("Парсинг данных...");
        List<T> data = parseData(content);

        System.out.println("Валидация данных...");
        validateData(data);

        System.out.println("Импортировано записей: " + data.size());
        return data;
    }

    protected String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    /**
     * Шаг парсинга — реализуется в наследниках в зависимости от формата файла.
     */
    protected abstract List<T> parseData(String content);

    /**
     * Базовая валидация: не допускаются пустые или null-списки.
     * Переопределяется при необходимости.
     */
    protected void validateData(List<T> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Данные пусты или не валидны");
        }
    }

    /**
     * Хук, вызываемый после импорта. По умолчанию — без действий.
     */
    protected void afterImport(List<T> data) {}
}
