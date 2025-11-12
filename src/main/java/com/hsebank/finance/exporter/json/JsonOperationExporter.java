package com.hsebank.finance.exporter.json;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Экспортер операций в JSON формат
 */
public class JsonOperationExporter implements DataExportVisitor {
    private final StringBuilder builder = new StringBuilder("[\n");
    private boolean first = true;

    @Override
    public void visit(Operation operation) {
        if (!first) builder.append(",\n");
        first = false;

        builder.append("  {\n")
                .append("    \"id\": \"").append(operation.getId()).append("\",\n")
                .append("    \"type\": \"").append(escape(operation.getType().toString())).append("\",\n")
                .append("    \"bankAccountId\": \"").append(operation.getBankAccountId()).append("\",\n")
                .append("    \"amount\": ").append(operation.getAmount()).append(",\n")
                .append("    \"date\": \"").append(escape(operation.getDate().toString())).append("\",\n")
                .append("    \"description\": \"").append(escape(operation.getDescription())).append("\",\n")
                .append("    \"categoryId\": \"").append(operation.getCategoryId()).append("\"\n")
                .append("  }");
    }

    @Override public void visit(BankAccount account) {}
    @Override public void visit(Category category) {}

    public String getJson() {
        return builder.append("\n]").toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\"");
    }
}