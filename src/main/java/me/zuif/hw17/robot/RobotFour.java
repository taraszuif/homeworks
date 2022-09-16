package me.zuif.hw17.robot;

import me.zuif.hw17.Factory;

import java.util.Random;

public class RobotFour extends Thread {
    private volatile int microchipsProgress;

    protected RobotFour() {
        this.microchipsProgress = 0;
    }

    @Override
    public void run() {
        System.out.println("RobotFour started. Thread: " + Thread.currentThread().getName());
        do {
            int progress = new Random().nextInt(25, 36);

            try {
                var d = Math.random();
                if (d > 0.3) {
                    microchipsProgress += progress;
                    System.out.println("RobotFour made progress " + progress + ". Total: " + microchipsProgress);
                } else {
                    System.out.println("RobotFour failed to make progress! Total: " + microchipsProgress);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (microchipsProgress < 100);

        Factory factory = Factory.getInstance();
        factory.setProgrammedMicrochipsCount(factory.getProgrammedMicrochipsCount() + 1);
        System.out.println("RobotFour add programmed microchips to factory. Total: " + factory.getProgrammedMicrochipsCount());
        microchipsProgress = 0;
        System.out.println("RobotFour disabled");
        interrupt();
    }
}
