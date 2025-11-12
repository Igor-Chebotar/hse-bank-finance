package com.hsebank.finance.console;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.facade.BankAccountFacade;
import com.hsebank.finance.facade.CategoryFacade;
import com.hsebank.finance.facade.OperationFacade;
import com.hsebank.finance.importer.DataImporter;
import com.hsebank.finance.importer.csv.CsvBankAccountImporter;
import com.hsebank.finance.importer.csv.CsvCategoryImporter;
import com.hsebank.finance.importer.csv.CsvOperationImporter;
import com.hsebank.finance.importer.json.JsonBankAccountImporter;
import com.hsebank.finance.importer.json.JsonCategoryImporter;
import com.hsebank.finance.importer.json.JsonOperationImporter;

import java.util.List;

/**
 * Сервис импорта данных из файлов (JSON, CSV)
 * Использует паттерн Template Method для импорта
 */
public class ImportService {
    private final CategoryFacade categoryFacade;
    private final BankAccountFacade accountFacade;
    private final OperationFacade operationFacade;
    private final ConsoleHelper helper;

    public ImportService(CategoryFacade categoryFacade,
                         BankAccountFacade accountFacade,
                         OperationFacade operationFacade,
                         ConsoleHelper helper) {
        this.categoryFacade = categoryFacade;
        this.accountFacade = accountFacade;
        this.operationFacade = operationFacade;
        this.helper = helper;
    }

    public void importFromJson() {
        System.out.println("─── Импорт из JSON ───");
        System.out.println("1. Категории");
        System.out.println("2. Счета");
        System.out.println("3. Операции");

        int choice = helper.getIntInput("Выберите тип данных: ");
        String filePath = helper.getStringInput("Введите путь к файлу: ");

        try {
            switch (choice) {
                case 1:
                    importCategoriesFromJson(filePath);
                    break;
                case 2:
                    importAccountsFromJson(filePath);
                    break;
                case 3:
                    importOperationsFromJson(filePath);
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void importCategoriesFromJson(String filePath) {
        DataImporter<Category> importer = new JsonCategoryImporter();

        try {
            List<Category> categories = importer.importFromFile(filePath);

            for (Category category : categories) {
                categoryFacade.getCategoryById(category.getId()).ifPresentOrElse(
                        existing -> System.out.println("Категория с ID " + category.getId() + " уже существует"),
                        () -> {
                            Category newCategory = new Category(
                                    category.getId(),
                                    category.getType(),
                                    category.getName()
                            );
                            categoryFacade.updateCategory(newCategory);
                            System.out.println("Импортирована: " + category.getName());
                        }
                );
            }

            System.out.println("\nИмпорт завершён!");

        } catch (Exception e) {
            System.out.println("\nОшибка импорта: " + e.getMessage());
        }
    }

    private void importAccountsFromJson(String filePath) {
        DataImporter<BankAccount> importer = new JsonBankAccountImporter();

        try {
            List<BankAccount> accounts = importer.importFromFile(filePath);

            for (BankAccount account : accounts) {
                accountFacade.getBankAccountById(account.getId()).ifPresentOrElse(
                        existing -> System.out.println("Счёт с ID " + account.getId() + " уже существует"),
                        () -> {
                            BankAccount newAccount = new BankAccount(
                                    account.getId(),
                                    account.getName(),
                                    account.getBalance()
                            );
                            accountFacade.updateBankAccount(newAccount);
                            System.out.println("Импортирован: " + account.getName());
                        }
                );
            }

            System.out.println("\nИмпорт завершён!");

        } catch (Exception e) {
            System.out.println("\nОшибка импорта: " + e.getMessage());
        }
    }

    private void importOperationsFromJson(String filePath) {
        DataImporter<Operation> importer = new JsonOperationImporter();

        try {
            List<Operation> operations = importer.importFromFile(filePath);

            for (Operation operation : operations) {
                operationFacade.getOperationById(operation.getId()).ifPresentOrElse(
                        existing -> System.out.println("Операция с ID " + operation.getId() + " уже существует"),
                        () -> {
                            Operation newOperation = new Operation(
                                    operation.getId(),
                                    operation.getType(),
                                    operation.getBankAccountId(),
                                    operation.getAmount(),
                                    operation.getDate(),
                                    operation.getCategoryId()
                            );
                            newOperation.setDescription(operation.getDescription());
                            System.out.println("Импортирована операция: " + operation.getType() +
                                    " на сумму " + operation.getAmount() + "₽");
                        }
                );
            }

            System.out.println("\nИмпорт операций завершён!");

        } catch (Exception e) {
            System.out.println("\nОшибка импорта: " + e.getMessage());
        }
    }

    public void importFromCsv() {
        System.out.println("─── Импорт из CSV ───");
        System.out.println("1. Категории");
        System.out.println("2. Счета");
        System.out.println("3. Операции");

        int choice = helper.getIntInput("Выберите тип данных: ");
        String filePath = helper.getStringInput("Введите путь к файлу: ");

        try {
            switch (choice) {
                case 1:
                    importCategoriesFromCsv(filePath);
                    break;
                case 2:
                    importAccountsFromCsv(filePath);
                    break;
                case 3:
                    importOperationsFromCsv(filePath);
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void importCategoriesFromCsv(String filePath) {
        DataImporter<Category> importer = new CsvCategoryImporter();

        try {
            List<Category> categories = importer.importFromFile(filePath);

            for (Category category : categories) {
                categoryFacade.getCategoryById(category.getId()).ifPresentOrElse(
                        existing -> System.out.println("Категория с ID " + category.getId() + " уже существует"),
                        () -> {
                            Category newCategory = new Category(
                                    category.getId(),
                                    category.getType(),
                                    category.getName()
                            );
                            categoryFacade.updateCategory(newCategory);
                            System.out.println("Импортирована: " + category.getName());
                        }
                );
            }

            System.out.println("\nИмпорт завершён!");

        } catch (Exception e) {
            System.out.println("\nОшибка импорта: " + e.getMessage());
        }
    }

    private void importAccountsFromCsv(String filePath) {
        DataImporter<BankAccount> importer = new CsvBankAccountImporter();

        try {
            List<BankAccount> accounts = importer.importFromFile(filePath);

            for (BankAccount account : accounts) {
                accountFacade.getBankAccountById(account.getId()).ifPresentOrElse(
                        existing -> System.out.println("Счёт с ID " + account.getId() + " уже существует"),
                        () -> {
                            BankAccount newAccount = new BankAccount(
                                    account.getId(),
                                    account.getName(),
                                    account.getBalance()
                            );
                            accountFacade.updateBankAccount(newAccount);
                            System.out.println("Импортирован: " + account.getName());
                        }
                );
            }

            System.out.println("\nИмпорт завершён!");

        } catch (Exception e) {
            System.out.println("\nОшибка импорта: " + e.getMessage());
        }
    }

    private void importOperationsFromCsv(String filePath) {
        DataImporter<Operation> importer = new CsvOperationImporter();

        try {
            List<Operation> operations = importer.importFromFile(filePath);

            for (Operation operation : operations) {
                operationFacade.getOperationById(operation.getId()).ifPresentOrElse(
                        existing -> System.out.println("Операция с ID " + operation.getId() + " уже существует"),
                        () -> {
                            Operation newOperation = new Operation(
                                    operation.getId(),
                                    operation.getType(),
                                    operation.getBankAccountId(),
                                    operation.getAmount(),
                                    operation.getDate(),
                                    operation.getCategoryId()
                            );
                            newOperation.setDescription(operation.getDescription());
                            System.out.println("Импортирована операция: " + operation.getType() +
                                    " на сумму " + operation.getAmount() + "₽");
                        }
                );
            }

            System.out.println("\nИмпорт операций завершён!");

        } catch (Exception e) {
            System.out.println("\nОшибка импорта: " + e.getMessage());
        }
    }
}
