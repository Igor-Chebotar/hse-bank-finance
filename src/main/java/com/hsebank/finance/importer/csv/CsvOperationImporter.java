package com.hsebank.finance.importer.csv;

import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.domain.model.OperationType;
import com.hsebank.finance.importer.DataImporter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Импорт финансовых операций из CSV-файла.
 * Обрабатывает строки формата: id,type,bankAccountId,amount,date,categoryId,description
 */
public class CsvOperationImporter extends DataImporter<Operation> {

    @Override
    protected List<Operation> parseData(String content) {
        List<Operation> operations = new ArrayList<>();
        String[] lines = content.split("\n");

        // Пропуск заголовка
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length < 7) {
                System.out.println("Пропущена некорректная строка: " + line);
                continue;
            }

            try {
                String id = parts[0].replace("\"", "").trim();
                String type = parts[1].replace("\"", "").trim();
                String bankAccountId = parts[2].replace("\"", "").trim();
                String amountStr = parts[3].replace("\"", "").trim();
                String dateStr = parts[4].replace("\"", "").trim();
                String categoryId = parts[5].replace("\"", "").trim();
                String description = parts[6].replace("\"", "").trim();

                Operation operation = new Operation();
                operation.setId(id);
                operation.setType(OperationType.valueOf(type));
                operation.setBankAccountId(bankAccountId);
                operation.setAmount(new BigDecimal(amountStr));
                operation.setDate(LocalDate.parse(dateStr));
                operation.setCategoryId(categoryId);
                operation.setDescription(description);

                operations.add(operation);
            } catch (Exception e) {
                System.out.println("Ошибка парсинга строки: " + line);
                System.out.println("   Причина: " + e.getMessage());
            }
        }

        return operations;
    }

    @Override
    protected void validateData(List<Operation> data) {
        super.validateData(data);

        for (Operation operation : data) {
            if (operation.getType() == null) {
                throw new IllegalArgumentException("Найдена операция без типа");
            }
            if (operation.getAmount() == null || operation.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Найдена операция с некорректной суммой: " + operation.getAmount());
            }
            if (operation.getBankAccountId() == null || operation.getBankAccountId().trim().isEmpty()) {
                throw new IllegalArgumentException("Найдена операция без ID счёта");
            }
            if (operation.getCategoryId() == null || operation.getCategoryId().trim().isEmpty()) {
                throw new IllegalArgumentException("Найдена операция без ID категории");
            }
            if (operation.getDate() == null) {
                throw new IllegalArgumentException("Найдена операция без даты");
            }
        }
    }

    @Override
    protected void afterImport(List<Operation> data) {
        System.out.println("Импортировано операций:");
        int incomeCount = 0;
        int expenseCount = 0;

        for (Operation op : data) {
            if (op.getType() == OperationType.INCOME) {
                incomeCount++;
            } else {
                expenseCount++;
            }
        }

        System.out.println("   Доходов: " + incomeCount);
        System.out.println("   Расходов: " + expenseCount);
    }
}
