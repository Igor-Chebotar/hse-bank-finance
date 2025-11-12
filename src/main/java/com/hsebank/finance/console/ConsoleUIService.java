package com.hsebank.finance.console;

import com.hsebank.finance.command.*;
import com.hsebank.finance.decorator.TimingDecorator;
import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.domain.model.OperationType;
import com.hsebank.finance.facade.BankAccountFacade;
import com.hsebank.finance.facade.CategoryFacade;
import com.hsebank.finance.facade.OperationFacade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис опроса пользователя: задаёт вопросы и формирует DTO для команд.
 */
public class ConsoleUIService {
    private final CategoryFacade categoryFacade;
    private final BankAccountFacade accountFacade;
    private final OperationFacade operationFacade;
    private final ConsoleHelper helper;

    public ConsoleUIService(CategoryFacade categoryFacade,
                            BankAccountFacade accountFacade,
                            OperationFacade operationFacade,
                            ConsoleHelper helper) {
        this.categoryFacade = categoryFacade;
        this.accountFacade = accountFacade;
        this.operationFacade = operationFacade;
        this.helper = helper;
    }

    public void createCategory() {
        System.out.println("─── Создание категории ───");

        System.out.println("Тип категории:");
        System.out.println("1 - Доход (INCOME)");
        System.out.println("2 - Расход (EXPENSE)");
        int typeChoice = helper.getIntInput("Выберите тип: ");

        OperationType type = (typeChoice == 1) ? OperationType.INCOME : OperationType.EXPENSE;

        String name = helper.getStringInput("Введите название категории: ");

        CreateCategoryCommand command = new CreateCategoryCommand(categoryFacade, type, name);
        Command withTiming = new TimingDecorator(command);

        try {
            withTiming.execute();
            Category result = command.getResult();

            System.out.println("\nКатегория успешно создана!");
            System.out.println("   Название: " + result.getName());
            System.out.println("   Тип: " + result.getType());
            System.out.println("   ID: " + result.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }
    }

    public void createAccount() {
        System.out.println("─── Создание банковского счёта ───");

        String name = helper.getStringInput("Введите название: ");
        BigDecimal balance = helper.getBigDecimalInput("Введите баланс: ");

        CreateBankAccountCommand command = new CreateBankAccountCommand(accountFacade, name, balance);
        Command withTiming = new TimingDecorator(command);

        try {
            withTiming.execute();
            BankAccount result = command.getResult();

            System.out.println("\nБанковский счёт успешно создан!");
            System.out.println("   Название: " + result.getName());
            System.out.println("   Баланс: " + result.getBalance() + "₽");
            System.out.println("   ID: " + result.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }
    }

    public void createIncome() {
        System.out.println("─── Добавление дохода ───");

        List<BankAccount> accounts = accountFacade.getAllBankAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Сначала создайте банковский счёт!");
            return;
        }

        System.out.println("\nДоступные счета:");
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount acc = accounts.get(i);
            System.out.println((i + 1) + ". " + acc.getName() + " (Баланс: " + acc.getBalance() + "₽)");
        }

        int accountChoice = helper.getIntInput("\nВыберите номер счёта: ");

        if (accountChoice < 1 || accountChoice > accounts.size()) {
            System.out.println("Неверный номер счёта!");
            return;
        }

        BankAccount selectedAccount = accounts.get(accountChoice - 1);

        List<Category> allCategories = categoryFacade.getAllCategories();
        List<Category> incomeCategories = new ArrayList<>();

        for (Category cat : allCategories) {
            if (cat.getType() == OperationType.INCOME) {
                incomeCategories.add(cat);
            }
        }

        if (incomeCategories.isEmpty()) {
            System.out.println("Сначала создайте категорию доходов!");
            return;
        }

        System.out.println("\nКатегории доходов:");
        for (int i = 0; i < incomeCategories.size(); i++) {
            Category cat = incomeCategories.get(i);
            System.out.println((i + 1) + ". " + cat.getName());
        }

        int categoryChoice = helper.getIntInput("\nВыберите номер категории: ");

        if (categoryChoice < 1 || categoryChoice > incomeCategories.size()) {
            System.out.println("Неверный номер категории!");
            return;
        }

        Category selectedCategory = incomeCategories.get(categoryChoice - 1);

        BigDecimal amount = helper.getBigDecimalInput("Введите сумму: ");


        String description = helper.getStringInput("Введите описание (или Enter, чтобы пропустить): ");

        if (description.trim().isEmpty()) {
            description = "Без описания";
        }

        CreateIncomeCommand command = new CreateIncomeCommand(operationFacade,
                selectedAccount.getId(),
                amount,
                selectedCategory.getId(),
                description);

        Command withTiming = new TimingDecorator(command);

        try {
            withTiming.execute();

            Operation result = command.getResult();

            System.out.println("\nДоход успешно добавлен!");
            System.out.println("   Название счёта: " + selectedAccount.getName());
            System.out.println("   Сумма: " + result.getAmount() + "₽");
            System.out.println("   Категория: " + selectedCategory.getName());
            System.out.println("   Описание: " + result.getDescription());

            BankAccount updatedAccount = accountFacade.getBankAccountById(selectedAccount.getId()).get();
            System.out.println("\n   Новый баланс счёта: " + updatedAccount.getBalance() + "₽");

        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }
    }

    public void createExpense() {
        System.out.println("─── Добавление расхода ───");

        List<BankAccount> accounts = accountFacade.getAllBankAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Сначала создайте банковский счёт!");
            return;
        }

        System.out.println("\nДоступные счета:");
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount acc = accounts.get(i);
            System.out.println((i + 1) + ". " + acc.getName() + " (Баланс: " + acc.getBalance() + "₽)");
        }

        int accountChoice = helper.getIntInput("\nВыберите номер счёта: ");

        if (accountChoice < 1 || accountChoice > accounts.size()) {
            System.out.println("Неверный номер счёта!");
            return;
        }

        BankAccount selectedAccount = accounts.get(accountChoice - 1);

        List<Category> allCategories = categoryFacade.getAllCategories();
        List<Category> expenseCategories = new ArrayList<>();

        for (Category cat : allCategories) {
            if (cat.getType() == OperationType.EXPENSE) {
                expenseCategories.add(cat);
            }
        }

        if (expenseCategories.isEmpty()) {
            System.out.println("Сначала создайте категорию расходов!");
            return;
        }

        System.out.println("\nКатегории расходов:");
        for (int i = 0; i < expenseCategories.size(); i++) {
            Category cat = expenseCategories.get(i);
            System.out.println((i + 1) + ". " + cat.getName());
        }

        int categoryChoice = helper.getIntInput("\nВыберите номер категории: ");

        if (categoryChoice < 1 || categoryChoice > expenseCategories.size()) {
            System.out.println("Неверный номер категории!");
            return;
        }

        Category selectedCategory = expenseCategories.get(categoryChoice - 1);

        BigDecimal amount = helper.getBigDecimalInput("Введите сумму: ");


        String description = helper.getStringInput("Введите описание (или Enter, чтобы пропустить): ");

        if (description.trim().isEmpty()) {
            description = "Без описания";
        }

        CreateExpenseCommand command = new CreateExpenseCommand(operationFacade,
                selectedAccount.getId(),
                amount,
                selectedCategory.getId(),
                description);

        Command withTiming = new TimingDecorator(command);

        try {
            withTiming.execute();

            Operation result = command.getResult();

            System.out.println("\nРасход успешно добавлен!");
            System.out.println("   Название счёта: " + selectedAccount.getName());
            System.out.println("   Сумма: " + result.getAmount() + "₽");
            System.out.println("   Категория: " + selectedCategory.getName());
            System.out.println("   Описание: " + result.getDescription());

            BankAccount updatedAccount = accountFacade.getBankAccountById(selectedAccount.getId()).get();
            System.out.println("\n   Новый баланс счёта: " + updatedAccount.getBalance() + "₽");

        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }

    }

    public void showAllCategories() {
        System.out.println("─── Все категории ───");

        List<Category> categories = categoryFacade.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("Категорий пока нет");
            return;
        }

        for (Category cat : categories) {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Название: " + cat.getName());
            System.out.println("Тип: " + cat.getType());
            System.out.println("ID: " + cat.getId());
        }

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Всего категорий: " + categories.size());
    }

    public void showAllAccounts() {
        System.out.println("─── Все банковские счета ───");

        List<BankAccount> accounts = accountFacade.getAllBankAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Счетов пока нет");
            return;
        }

        for (BankAccount acc : accounts) {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Название: " + acc.getName());
            System.out.println("Баланс: " + acc.getBalance() + "₽");
            System.out.println("ID: " + acc.getId());
        }

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Всего счетов: " + accounts.size());
    }

    public void showAllOperations() {
        System.out.println("─── Все операции ───");

        List<Operation> operations = operationFacade.getAllOperations();

        if (operations.isEmpty()) {
            System.out.println("Операций пока нет");
            return;
        }

        for (Operation op : operations) {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Тип: " + (op.getType() == OperationType.INCOME ? "Доход" : "Расход"));
            System.out.println("Сумма: " + op.getAmount() + "₽");
            System.out.println("Дата: " + op.getDate());
            System.out.println("Описание: " + op.getDescription());

            // Показываем названия вместо ID
            accountFacade.getBankAccountById(op.getBankAccountId()).ifPresent(
                    acc -> System.out.println("Счёт: " + acc.getName())
            );

            categoryFacade.getCategoryById(op.getCategoryId()).ifPresent(
                    cat -> System.out.println("Категория: " + cat.getName())
            );
        }

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Всего операций: " + operations.size());
    }

}
