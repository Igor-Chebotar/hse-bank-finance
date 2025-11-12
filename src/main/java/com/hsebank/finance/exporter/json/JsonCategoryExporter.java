package com.hsebank.finance.exporter.json;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Экспортер категорий в JSON формат
 */
public class JsonCategoryExporter implements DataExportVisitor {
    private final StringBuilder builder = new StringBuilder("[\n");
    private boolean first = true;

    @Override
    public void visit(Category category) {
        if (!first) builder.append(",\n");
        first = false;

        builder.append("  {\n")
                .append("    \"id\": \"").append(category.getId()).append("\",\n")
                .append("    \"type\": \"").append(escape(category.getType().toString())).append("\",\n")
                .append("    \"name\": \"").append(escape(category.getName())).append("\"\n")
                .append("  }");
    }

    @Override public void visit(BankAccount account) {}
    @Override public void visit(Operation operation) {}

    public String getJson() {
        return builder.append("\n]").toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\"");
    }
}

