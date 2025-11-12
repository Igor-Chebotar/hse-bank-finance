package com.hsebank.finance.importer.csv;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.importer.DataImporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Импорт банковских счетов из CSV-файла.
 * Реализация шагов парсинга и валидации для формата CSV.
 */
public class CsvBankAccountImporter extends DataImporter<BankAccount> {

    @Override
    protected List<BankAccount> parseData(String content) {
        List<BankAccount> accounts = new ArrayList<>();

        String[] lines = content.split("\n");

        // Пропуск заголовка
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            if (parts.length < 3) {
                System.out.println("Пропущена некорректная строка: " + line);
                continue;
            }

            String id = parts[0].replace("\"", "").trim();
            String name = parts[1].replace("\"", "").trim();
            String balanceStr = parts[2].replace("\"", "").trim();

            BankAccount account = new BankAccount();
            account.setId(id);
            account.setName(name);
            account.setBalance(new BigDecimal(balanceStr));

            accounts.add(account);
        }

        return accounts;
    }

    @Override
    protected void validateData(List<BankAccount> data) {
        super.validateData(data);

        // Проверка, что у всех счетов задано имя и баланс
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
