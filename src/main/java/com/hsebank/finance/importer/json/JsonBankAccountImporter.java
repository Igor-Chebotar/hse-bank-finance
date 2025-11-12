package com.hsebank.finance.importer.json;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.importer.DataImporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Импорт банковских счетов из JSON-строки.
 * Использует регулярное выражение для парсинга объектов в формате:
 * {"id":"...", "name":"...", "balance":"..."}
 */
public class JsonBankAccountImporter extends DataImporter<BankAccount> {

    @Override
    protected List<BankAccount> parseData(String content) {
        List<BankAccount> accounts = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "\\{\\s*\"id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*" +
                        "\"balance\"\\s*:\\s*\"([^\"]+)\"\\s*\\}"
        );

        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String id = matcher.group(1);
            String name = matcher.group(2);
            String balance = matcher.group(3);

            BankAccount account = new BankAccount();
            account.setId(id);
            account.setName(name);
            account.setBalance(new BigDecimal(balance));

            accounts.add(account);
        }

        return accounts;
    }

    @Override
    protected void validateData(List<BankAccount> data) {
        super.validateData(data);

        for (BankAccount account : data) {
            if (account.getName() == null || account.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Найден счёт с пустым названием");
            }
            if (account.getBalance() == null) {
                throw new IllegalArgumentException("Найден счёт без баланса");
            }
        }
    }
}
