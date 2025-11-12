package com.hsebank.finance.importer.csv;

import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.OperationType;
import com.hsebank.finance.importer.DataImporter;

import java.util.ArrayList;
import java.util.List;

/**
 * Импорт категорий операций из CSV-файла.
 * Определяет логику парсинга и валидации для категорий.
 */
public class CsvCategoryImporter extends DataImporter<Category> {

    @Override
    protected List<Category> parseData(String content) {
        List<Category> categories = new ArrayList<>();
        String[] lines = content.split("\n");

        // Пропуск заголовка
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length < 3) {
                System.out.println("Пропущена некорректная строка: " + line);
                continue;
            }

            String id = parts[0].replace("\"", "").trim();
            String type = parts[1].replace("\"", "").trim();
            String name = parts[2].replace("\"", "").trim();

            Category category = new Category();
            category.setId(id);
            category.setType(OperationType.valueOf(type));
            category.setName(name);

            categories.add(category);
        }

        return categories;
    }

    @Override
    protected void validateData(List<Category> data) {
        super.validateData(data);

        for (Category category : data) {
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Найдена категория с пустым названием");
            }
            if (category.getType() == null) {
                throw new IllegalArgumentException("Найдена категория без типа");
            }
        }
    }
}
