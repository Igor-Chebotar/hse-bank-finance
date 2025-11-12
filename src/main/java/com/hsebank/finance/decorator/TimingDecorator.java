package com.hsebank.finance.decorator;

import com.hsebank.finance.command.Command;

/**
 * Декоратор для измерения времени выполнения команд
 */
public class TimingDecorator implements Command {
    private final Command wrappedCommand;

    public TimingDecorator(Command wrappedCommand) {
        this.wrappedCommand = wrappedCommand;
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();

        wrappedCommand.execute();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("⏱️ Команда выполнена за " + duration + " мс");
    }
}