package me.zuif.hw2.command;

import java.util.Scanner;

public interface ICommand {
    Scanner SCANNER = new Scanner(System.in);

    void execute();
}
