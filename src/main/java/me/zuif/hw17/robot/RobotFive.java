package me.zuif.hw17.robot;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw17.Factory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RobotFive extends Thread {
    @Getter
    @Setter
    private AtomicBoolean isAlive = new AtomicBoolean(true);
    private volatile int detailFormProgress;

    @Override
    public void run() {
        System.out.println("RobotFive started. Thread: " + Thread.currentThread().getName());
        while (isAlive.get()) {
            int fuelNeed = new Random().nextInt(350, 701);
            System.out.println("RobotFive need " + fuelNeed + " to work");
            Factory factory = Factory.getInstance();
            while (!(factory.getFuelCount() > fuelNeed)) {
                Thread.yield();
            }
            factory.setFuelCount(factory.getFuelCount() - fuelNeed);
            System.out.println("RobotFive get fuel and make progress. Total: " + detailFormProgress + ". Fuel left: " + factory.getFuelCount());
            detailFormProgress += 10;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (detailFormProgress >= 100) {
                factory.setProgrammedMicrochipsCount(factory.getProgrammedMicrochipsCount() - 1);
                factory.setDetailConstructionCount(factory.getDetailConstructionCount() - 1);
                factory.setDetailCount(factory.getDetailCount() + 1);
                System.out.println("RobotFive end make detail and add it to factory, remove microchip and detail construction. Total details: " + factory.getDetailCount());
                detailFormProgress = 0;
            }
        }
        System.out.println("RobotFive disabled");
        interrupt();
    }
}
