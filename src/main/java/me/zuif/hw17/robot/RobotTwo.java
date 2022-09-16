package me.zuif.hw17.robot;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public class RobotTwo extends Thread {
    private final Exchanger<Integer> exchanger;
    private volatile int detailConstructionAssemblyProgress;
    @Getter
    @Setter
    private AtomicBoolean isAlive = new AtomicBoolean(true);

    public RobotTwo(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
        detailConstructionAssemblyProgress = 0;
    }

    @Override
    public void run() {
        System.out.println("RobotTwo started. Thread: " + Thread.currentThread().getName());
        while (isAlive.get()) {
            int progress = new Random().nextInt(5, 11);
            detailConstructionAssemblyProgress += progress;
            System.out.println("RobotTwo progress by " + progress + " and sleep 2 sec. Total: " + detailConstructionAssemblyProgress);
            try {
                Thread.sleep(2000);

                int exchanged = exchanger.exchange(detailConstructionAssemblyProgress);
                System.out.println("RobotTwo get progress of RobotThree and is " + exchanged);
                if (exchanged + detailConstructionAssemblyProgress >= 100) {
                    System.out.println("RobotTwo: overall progress reached 100! set progress to 0");
                    detailConstructionAssemblyProgress = 0;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("RobotTwo disabled");
        interrupt();
    }
}
