package com.hsebank.finance;

import com.hsebank.finance.console.ConsoleApplication;
import com.hsebank.finance.di.DIContainer;
import com.hsebank.finance.domain.model.Category;
import com.hsebank.finance.domain.model.OperationType;
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

/**
 * Точка входа с использованием DI-контейнера.
 * Инициализирует компоненты приложения и запускает консольный интерфейс.
 */
public class MainWithDI {

    /**
     * Регистрирует зависимости в DI-контейнере и запускает приложение.
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   ВШЭ-Банк с DI-контейнером                    ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        DIContainer container = new DIContainer();

        // Регистрация фабрик
        container.register(CategoryFactory.class);
        container.register(BankAccountFactory.class);
        container.register(OperationFactory.class);

        // Регистрация репозиториев (общая и конкретные реализации)
        container.register(Repository.class, InMemoryCategoryRepository.class);
        container.registerSingleton(InMemoryCategoryRepository.class, new InMemoryCategoryRepository());
        container.registerSingleton(InMemoryBankAccountRepository.class, new InMemoryBankAccountRepository());
        container.registerSingleton(InMemoryOperationRepository.class, new InMemoryOperationRepository());

        // Регистрация фасадов
        container.register(CategoryFacade.class);
        container.register(BankAccountFacade.class);
        container.register(OperationFacade.class);

        container.printBeans();
        System.out.println("DI-контейнер настроен!\n");

        demonstrateDI(container);

        System.out.println("\nЗапуск консольного приложения...\n");
        ConsoleApplication app = new ConsoleApplication();
        app.run();
    }

    /**
     * Демонстрация работы DI-контейнера: создание, сохранение и проверка синглтонов.
     */
    private static void demonstrateDI(DIContainer container) {
        System.out.println("═══ Демонстрация DI-контейнера ═══\n");

        System.out.println("1 Получение CategoryFactory из контейнера:");
        CategoryFactory factory1 = container.resolve(CategoryFactory.class);
        CategoryFactory factory2 = container.resolve(CategoryFactory.class);
        System.out.println("   factory1 == factory2: " + (factory1 == factory2));
        System.out.println("   → Контейнер вернул один и тот же экземпляр (Singleton)\n");

        System.out.println("2 Создание категории через фабрику:");
        Category category = factory1.create(OperationType.INCOME, "Зарплата (DI)");
        System.out.println("   Создана: " + category.getName() + " (ID: " + category.getId() + ")\n");

        System.out.println("3 Сохранение в репозиторий:");
        InMemoryCategoryRepository repo = container.resolve(InMemoryCategoryRepository.class);
        repo.save(category);
        System.out.println("   Сохранено в репозитории\n");

        System.out.println("4 Проверка Singleton паттерна:");
        InMemoryCategoryRepository repo2 = container.resolve(InMemoryCategoryRepository.class);
        System.out.println("   repo1 == repo2: " + (repo == repo2));
        System.out.println("   Количество категорий в repo2: " + repo2.findAll().size());
        System.out.println("   → Данные сохранились, т.к. это один экземпляр!\n");

        System.out.println("═══════════════════════════════════════════════\n");
    }
}
