package com.hsebank.finance;

import com.hsebank.finance.console.ConsoleApplication;

/**
 * Точка входа в приложение.
 */
public class Main {

    /**
     * Инициализирует зависимости и запускает консольный интерфейс.
     */
    public static void main(String[] args) {
        ConsoleApplication app = new ConsoleApplication();
        app.run();
    }
}