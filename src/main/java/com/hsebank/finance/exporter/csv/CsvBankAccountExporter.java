package com.hsebank.finance.exporter.csv;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Экспортер банковских счетов в CSV формат
 */
public class CsvBankAccountExporter implements DataExportVisitor {
    private final StringBuilder builder = new StringBuilder("id,name,balance\n");

    @Override
    public void visit(BankAccount account) {
        builder.append(account.getId()).append(",")
                .append(escape(account.getName())).append(",")
                .append(account.getBalance()).append("\n");
    }

    @Override public void visit(Category category) {}
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