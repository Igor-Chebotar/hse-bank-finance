package com.hsebank.finance.importer.json;

import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.OperationType;
import com.hsebank.finance.importer.DataImporter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Импорт категорий из JSON-строки.
 * Используется регулярное выражение для извлечения объектов формата:
 * {"id":"...", "type":"...", "name":"..."}
 */
public class JsonCategoryImporter extends DataImporter<Category> {

    @Override
    protected List<Category> parseData(String content) {
        List<Category> categories = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "\\{\\s*\"id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"type\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"name\"\\s*:\\s*\"([^\"]+)\"\\s*\\}"
        );

        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String id = matcher.group(1);
            String type = matcher.group(2);
            String name = matcher.group(3);

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
