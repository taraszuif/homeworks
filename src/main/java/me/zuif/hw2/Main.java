package me.zuif.hw2;


import me.zuif.hw2.command.*;
import me.zuif.hw2.service.PenService;
import me.zuif.hw2.service.PhoneService;
import me.zuif.hw2.service.TeaService;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;

import java.util.List;

public class Main {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final TeaService TEA_SERVICE = TeaService.getInstance();
    private static final PenService PEN_SERVICE = PenService.getInstance();

    public static void main(String[] args) {
        final Commands[] values = Commands.values();
        final List<String> names = Utils.getNamesOfEnum(values);
        boolean exit = false;
        do {
            int commandIndex = UserInputUtil.getUserInput(values.length, names);
            switch (values[commandIndex]) {
                case DELETE -> new Delete().execute();
                case PRINT -> new Print().execute();
                case CREATE -> new Create().execute();
                case UPDATE -> new Update().execute();
                case EXIT -> exit = true;
            }

        } while (!exit);

    }

}

