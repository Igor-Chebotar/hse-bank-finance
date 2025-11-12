package com.hsebank.finance.exporter.csv;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Экспортер категорий в CSV формат
 */
public class CsvCategoryExporter implements DataExportVisitor {
    private final StringBuilder builder = new StringBuilder("id,type,name\n");

    @Override
    public void visit(Category category) {
        builder.append(category.getId()).append(",")
                .append(escape(category.getType().toString())).append(",")
                .append(escape(category.getName())).append("\n");
    }

    @Override public void visit(BankAccount account) {}
    @Override public void visit(Operation operation) {}

    public String getCsv() {
        return builder.toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}