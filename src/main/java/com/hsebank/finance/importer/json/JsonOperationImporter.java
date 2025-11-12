package com.hsebank.finance.importer.json;

import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.domain.model.OperationType;
import com.hsebank.finance.importer.DataImporter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Импорт финансовых операций из JSON-строки.
 * Ожидаемый формат:
 * {"id":"...", "type":"...", "bankAccountId":"...", "amount":"...", "date":"...", "categoryId":"...", "description":"..."}
 */
public class JsonOperationImporter extends DataImporter<Operation> {

    @Override
    protected List<Operation> parseData(String content) {
        List<Operation> operations = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "\\{\\s*\"id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"type\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"bankAccountId\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"amount\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"date\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"categoryId\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"description\"\\s*:\\s*\"([^\"]*?)\"\\s*\\}"
        );

        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String id = matcher.group(1);
            String type = matcher.group(2);
            String bankAccountId = matcher.group(3);
            String amount = matcher.group(4);
            String date = matcher.group(5);
            String categoryId = matcher.group(6);
            String description = matcher.group(7);

            Operation operation = new Operation();
            operation.setId(id);
            operation.setType(OperationType.valueOf(type));
            operation.setBankAccountId(bankAccountId);
            operation.setAmount(new BigDecimal(amount));
            operation.setDate(LocalDate.parse(date));
            operation.setCategoryId(categoryId);
            operation.setDescription(description);

            operations.add(operation);
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
                throw new IllegalArgumentException("Найдена операция с некорректной суммой");
            }
        }
    }
}
