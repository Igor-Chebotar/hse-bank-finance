package com.hsebank.finance.exporter.json;


import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Экспортер банковских счетов в JSON формат
 */
public class JsonBankAccountExporter implements DataExportVisitor {
    private final StringBuilder builder = new StringBuilder("[\n");
    private boolean first = true;

    @Override
    public void visit(BankAccount account) {
        if (!first) builder.append(",\n");
        first = false;

        builder.append("  {\n")
                .append("    \"id\": \"").append(account.getId()).append("\",\n")
                .append("    \"name\": ").append("\"").append(escape(account.getName())).append("\",\n")
                .append("    \"balance\": ").append(account.getBalance()).append("\n")
                .append("  }");
    }

    @Override public void visit(Category category) {}
    @Override public void visit(Operation operation) {}

    public String getJson() {
        return builder.append("\n]").toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\""); // экранируем кавычки
    }
}