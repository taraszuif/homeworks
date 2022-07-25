package me.zuif.hw2.command;

import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.service.ProductFactory;
import me.zuif.hw2.util.UserInputUtil;
import me.zuif.hw2.util.Utils;

import java.util.List;

public class Create implements ICommand {
    @Override
    public void execute() {
        System.out.println("What do you want to create:");
        final ProductType[] values = ProductType.values();
        final List<String> names = Utils.getNamesOfType(values);
        final int userInput = UserInputUtil.getUserInput(values.length, names);
        ProductFactory.createAndSave(values[userInput]);
    }


}