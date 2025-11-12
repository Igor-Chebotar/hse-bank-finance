package com.hsebank.finance.console;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.exporter.csv.CsvBankAccountExporter;
import com.hsebank.finance.exporter.csv.CsvCategoryExporter;
import com.hsebank.finance.exporter.csv.CsvOperationExporter;
import com.hsebank.finance.exporter.json.JsonBankAccountExporter;
import com.hsebank.finance.exporter.json.JsonCategoryExporter;
import com.hsebank.finance.exporter.json.JsonOperationExporter;
import com.hsebank.finance.facade.BankAccountFacade;
import com.hsebank.finance.facade.CategoryFacade;
import com.hsebank.finance.facade.OperationFacade;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Сервис экспорта данных в файлы (JSON, CSV)
 * Использует паттерн Visitor для экспорта
 */
public class ExportService {
    private final CategoryFacade categoryFacade;
    private final BankAccountFacade accountFacade;
    private final OperationFacade operationFacade;
    private final ConsoleHelper helper;

    public ExportService(CategoryFacade categoryFacade,
                         BankAccountFacade accountFacade,
                         OperationFacade operationFacade,
                         ConsoleHelper helper) {
        this.categoryFacade = categoryFacade;
        this.accountFacade = accountFacade;
        this.operationFacade = operationFacade;
        this.helper = helper;
    }

    public void exportToJson() {
        System.out.println("─── Экспорт в JSON ───");
        System.out.println("1. Категории");
        System.out.println("2. Счета");
        System.out.println("3. Операции");

        int choice = helper.getIntInput("Выберите тип данных: ");
        String filePath = helper.getStringInput("Введите путь к файлу: ");

        try {
            switch (choice) {
                case 1:
                    exportCategoriesToJson(filePath);
                    break;
                case 2:
                    exportAccountsToJson(filePath);
                    break;
                case 3:
                    exportOperationsToJson(filePath);
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void exportCategoriesToJson(String filePath) throws IOException {
        List<Category> categories = categoryFacade.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("Нет категорий для экспорта");
            return;
        }

        JsonCategoryExporter exporter = new JsonCategoryExporter();

        for (Category category : categories) {
            category.accept(exporter);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(exporter.getJson());
        }

        System.out.println("Экспортировано категорий: " + categories.size());
        System.out.println("Файл: " + filePath);
    }

    private void exportAccountsToJson(String filePath) throws IOException {
        List<BankAccount> accounts = accountFacade.getAllBankAccounts();  // ← Получаем из фасада!

        if (accounts.isEmpty()) {
            System.out.println("Нет счетов для экспорта");
            return;
        }

        JsonBankAccountExporter exporter = new JsonBankAccountExporter();

        for (BankAccount account : accounts) {
            account.accept(exporter);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(exporter.getJson());
        }

        System.out.println("Экспортировано счетов: " + accounts.size());
        System.out.println("Файл: " + filePath);
    }

    private void exportOperationsToJson(String filePath) throws IOException {
        List<Operation> operations = operationFacade.getAllOperations();  // ← Получаем из фасада!

        if (operations.isEmpty()) {
            System.out.println("Нет операций для экспорта");
            return;
        }

        JsonOperationExporter exporter = new JsonOperationExporter();

        for (Operation operation : operations) {
            operation.accept(exporter);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(exporter.getJson());
        }

        System.out.println("Экспортировано операций: " + operations.size());
        System.out.println("Файл: " + filePath);
    }

    public void exportToCsv() {
        System.out.println("─── Экспорт в CSV ───");
        System.out.println("1. Категории");
        System.out.println("2. Счета");
        System.out.println("3. Операции");

        int choice = helper.getIntInput("Выберите тип данных: ");
        String filePath = helper.getStringInput("Введите путь к файлу: ");

        try {
            switch (choice) {
                case 1:
                    exportCategoriesToCsv(filePath);
                    break;
                case 2:
                    exportAccountsToCsv(filePath);
                    break;
                case 3:
                    exportOperationsToCsv(filePath);
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void exportCategoriesToCsv(String filePath) throws IOException {
        List<Category> categories = categoryFacade.getAllCategories();  // ← Получаем из фасада!

        if (categories.isEmpty()) {
            System.out.println("Нет категорий для экспорта");
            return;
        }

        CsvCategoryExporter exporter = new CsvCategoryExporter();

        for (Category category : categories) {
            category.accept(exporter);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(exporter.getCsv());
        }

        System.out.println("Экспортировано категорий: " + categories.size());
        System.out.println("Файл: " + filePath);
    }

    private void exportAccountsToCsv(String filePath) throws IOException {
        List<BankAccount> accounts = accountFacade.getAllBankAccounts();  // ← Получаем из фасада!

        if (accounts.isEmpty()) {
            System.out.println("Нет счетов для экспорта");
            return;
        }

        CsvBankAccountExporter exporter = new CsvBankAccountExporter();

        for (BankAccount account : accounts) {
            account.accept(exporter);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(exporter.getCsv());
        }

        System.out.println("Экспортировано счетов: " + accounts.size());
        System.out.println("Файл: " + filePath);
    }

    private void exportOperationsToCsv(String filePath) throws IOException {
        List<Operation> operations = operationFacade.getAllOperations();  // ← Получаем из фасада!

        if (operations.isEmpty()) {
            System.out.println("Нет операций для экспорта");
            return;
        }

        CsvOperationExporter exporter = new CsvOperationExporter();

        for (Operation operation : operations) {
            operation.accept(exporter);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(exporter.getCsv());
        }

        System.out.println("Экспортировано операций: " + operations.size());
        System.out.println("Файл: " + filePath);
    }
}
