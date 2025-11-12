package com.hsebank.finance.console;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Утилиты для чтения ввода и печати текстовых сообщений/таблиц.
 */
public class ConsoleHelper {
    private final Scanner scanner;

    public ConsoleHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // Убираем перевод строки
                return value;
            } catch (Exception e) {
                System.out.println("Ошибка ввода. Введите целое число.");
                scanner.nextLine(); // Очищаем буфер
            }
        }
    }

    public BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return new BigDecimal(input);
            } catch (Exception e) {
                System.out.println("Ошибка ввода. Введите число (например: 1000 или 1000.50)");
            }
        }
    }

    public String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void waitForEnter() {
        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }
}
