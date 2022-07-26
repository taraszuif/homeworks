package me.zuif.hw2.command;

import lombok.Getter;

@Getter
public enum Commands {
    CREATE("Create product", new Create()),
    UPDATE("Update product", new Update()),
    PRINT("Print products", new Print()),
    DELETE("Delete product", new Delete()),
    EXIT("Exit", null);

    private final String name;
    private final ICommand command;

    Commands(String name, ICommand command) {
        this.name = name;
        this.command = command;
    }
}
