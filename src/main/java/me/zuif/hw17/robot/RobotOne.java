package me.zuif.hw17.robot;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw17.Factory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RobotOne extends Thread {
    @Getter
    @Setter
    private AtomicBoolean isAlive = new AtomicBoolean(true);

    @Override
    public void run() {
        System.out.println("RobotOne started. Thread: " + Thread.currentThread().getName());
        while (isAlive.get()) {
            int fuelCount = new Random().nextInt(500, 1001);
            System.out.println("RobotOne mined " + fuelCount + " gallongs of fuel, transporting..");
            try {
                Thread.sleep(3000);
                Factory factory = Factory.getInstance();
                int total = factory.getFuelCount() + fuelCount;
                System.out.println("RobotOne transported " + fuelCount + " gallongs of fuel. Total: " + total);
                factory.setFuelCount(total);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("RobotOne disabled");
        interrupt();
    }
}
