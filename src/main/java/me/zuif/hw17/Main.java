package me.zuif.hw17;

import me.zuif.hw17.robot.RobotOne;
import me.zuif.hw17.robot.RobotThree;
import me.zuif.hw17.robot.RobotTwo;

import java.util.concurrent.Exchanger;

public class Main {
    public static void main(String[] args) {
        RobotOne robotOne = new RobotOne();
        Exchanger<Integer> exchanger = new Exchanger<>();
        RobotTwo robotTwo = new RobotTwo(exchanger);
        RobotThree robotThree = new RobotThree(exchanger);
        robotOne.start();
        robotTwo.start();
        robotThree.start();

    }
}
