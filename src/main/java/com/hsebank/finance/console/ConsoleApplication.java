package com.hsebank.finance.console;

import com.hsebank.finance.domain.model.BankAccount;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.Operation;
import com.hsebank.finance.facade.BankAccountFacade;
import com.hsebank.finance.facade.CategoryFacade;
import com.hsebank.finance.facade.OperationFacade;
import com.hsebank.finance.factory.BankAccountFactory;
import com.hsebank.finance.factory.CategoryFactory;
import com.hsebank.finance.factory.OperationFactory;
import com.hsebank.finance.repository.InMemoryBankAccountRepository;
import com.hsebank.finance.repository.InMemoryCategoryRepository;
import com.hsebank.finance.repository.InMemoryOperationRepository;
import com.hsebank.finance.repository.Repository;

import java.util.Scanner;

/**
 * Текстовый интерфейс приложения: инициализация и главный цикл обработки команд.
 */
public class ConsoleApplication {
    private final ConsoleUIService uiService;
    private final ImportService importService;
    private final ExportService exportService;
    private final ConsoleHelper helper;

    public ConsoleApplication() {
        Scanner scanner = new Scanner(System.in);

        // Создаём helper для работы с вводом
        this.helper = new ConsoleHelper(scanner);

        // Инициализируем фасады
        FacadeHolder facades = initializeFacades();

        // Создаём сервисы
        this.uiService = new ConsoleUIService(
                facades.categoryFacade,
                facades.accountFacade,
                facades.operationFacade,
                helper
        );

        this.importService = new ImportService(
                facades.categoryFacade,
                facades.accountFacade,
                facades.operationFacade,
                helper
        );

        this.exportService = new ExportService(
                facades.categoryFacade,
                facades.accountFacade,
                facades.operationFacade,
                helper
        );

        System.out.println("Система инициализирована\n");
    }

    private FacadeHolder initializeFacades() {
        // Создаём фабрики
        BankAccountFactory accountFactory = new BankAccountFactory();
        CategoryFactory categoryFactory = new CategoryFactory();
        OperationFactory operationFactory = new OperationFactory();

        // Создаём репозитории
        Repository<BankAccount, String> accountRepo = new InMemoryBankAccountRepository();
        Repository<Category, String> categoryRepo = new InMemoryCategoryRepository();
        Repository<Operation, String> operationRepo = new InMemoryOperationRepository();

        // Создаём фасады
        BankAccountFacade accountFacade = new BankAccountFacade(accountFactory, accountRepo);
        CategoryFacade categoryFacade = new CategoryFacade(categoryFactory, categoryRepo);
        OperationFacade operationFacade = new OperationFacade(operationFactory, operationRepo, accountRepo);

        return new FacadeHolder(categoryFacade, accountFacade, operationFacade);
    }

    /**
     * Главный цикл: читает команды пользователя, выполняет их и печатает результаты.
     */
    public void run() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   ВШЭ-Банк: Система учёта финансов    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        boolean running = true;
        while (running) {
            showMenu();
            int choice = helper.getIntInput("Выберите действие: ");
            System.out.println();

            running = handleChoice(choice);

            if (running) {
                helper.waitForEnter();
            }
        }

        System.out.println("\nДо свидания!");
    }

    private void showMenu() {
        System.out.println("═══════════════ ГЛАВНОЕ МЕНЮ ═══════════════");
        System.out.println("1. Создать категорию");
        System.out.println("2. Создать банковский счёт");
        System.out.println("3. Добавить доход");
        System.out.println("4. Добавить расход");
        System.out.println("5. Показать все категории");
        System.out.println("6. Показать все счета");
        System.out.println("7. Показать все операции");
        System.out.println("8. Импорт из JSON");
        System.out.println("9. Импорт из CSV");
        System.out.println("10. Экспорт в JSON");
        System.out.println("11. Экспорт в CSV");
        System.out.println("0. Выход");
        System.out.println("═══════════════════════════════════════════");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                uiService.createCategory();
                break;
            case 2:
                uiService.createAccount();
                break;
            case 3:
                uiService.createIncome();
                break;
            case 4:
                uiService.createExpense();
                break;
            case 5:
                uiService.showAllCategories();
                break;
            case 6:
                uiService.showAllAccounts();
                break;
            case 7:
                uiService.showAllOperations();
                break;
            case 8:
                importService.importFromJson();
                break;
            case 9:
                importService.importFromCsv();
                break;
            case 10:
                exportService.exportToJson();
                break;
            case 11:
                exportService.exportToCsv();
                break;
            case 0:
                return false; // Выход из программы
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }

        return true; // Продолжаем работу
    }

    private static class FacadeHolder {
        final CategoryFacade categoryFacade;
        final BankAccountFacade accountFacade;
        final OperationFacade operationFacade;

        FacadeHolder(CategoryFacade categoryFacade,
                     BankAccountFacade accountFacade,
                     OperationFacade operationFacade) {
            this.categoryFacade = categoryFacade;
            this.accountFacade = accountFacade;
            this.operationFacade = operationFacade;
        }
    }
}