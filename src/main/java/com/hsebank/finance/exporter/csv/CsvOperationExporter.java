package com.hsebank.finance.exporter.csv;


import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.DataExportVisitor;

/**
 * Экспортер операций в CSV формат
 */
public class CsvOperationExporter implements DataExportVisitor {
    private final StringBuilder builder = new StringBuilder("id,type,bankAccountId,amount,date,description,categoryId\n");

    @Override
    public void visit(Operation operation) {
        builder.append(escape(operation.getId())).append(",")
                .append(escape(operation.getType().toString())).append(",")
                .append(escape(operation.getBankAccountId())).append(",")
                .append(operation.getAmount()).append(",")
                .append(escape(operation.getDate().toString())).append(",")
                .append(escape(operation.getDescription() != null ? operation.getDescription() : "")).append(",")
                .append(escape(operation.getCategoryId())).append("\n");
    }

    @Override public void visit(BankAccount account) {}
    @Override public void visit(Category category) {}

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
