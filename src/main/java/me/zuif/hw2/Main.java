package me.zuif.hw2;


import me.zuif.hw2.command.Commands;
import me.zuif.hw2.command.ICommand;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.TeaService;

import static me.zuif.hw2.command.ICommand.SCANNER;

public class Main {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final TeaService TEA_SERVICE = TeaService.getInstance();
    private static final PenService PEN_SERVICE = PenService.getInstance();

    public static void main(String[] args) {
        final Commands[] values = Commands.values();
        boolean exit;
        do {
            exit = userAction(values);
        } while (!exit);

    }

    private static boolean userAction(final Commands[] values) {
        int userCommand = -1;
        do {
            for (int i = 0; i < values.length; i++) {
                System.out.printf("%d) %s%n", i, values[i].getName());
            }
            int input = SCANNER.nextInt();
            if (input >= 0 && input < values.length) {
                userCommand = input;
            }
        } while (userCommand == -1);
        final ICommand command = values[userCommand].getCommand();
        if (command == null) {
            return true;
        } else {
            command.execute();
            return false;
        }
    }
}
