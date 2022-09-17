package me.zuif.hw17.robot;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw17.Factory;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicBoolean;

public class RobotThree extends Thread {
    private final Exchanger<Integer> exchanger;
    private volatile int detailConstructionAssemblyProgress;
    @Getter
    @Setter
    private AtomicBoolean isAlive = new AtomicBoolean(true);

    public RobotThree(Exchanger<Integer> exchanger) {
        this.exchanger = exchanger;
        detailConstructionAssemblyProgress = 0;
    }

    @Override
    public void run() {
        System.out.println("RobotThree started. Thread: " + Thread.currentThread().getName());
        while (isAlive.get()) {
            int progress = new Random().nextInt(10, 21);
            detailConstructionAssemblyProgress += progress;
            System.out.println("RobotThree progress by " + progress + " and sleep 2 sec. Total: " + detailConstructionAssemblyProgress);

            try {
                Thread.sleep(2000);
                int exchanged = exchanger.exchange(detailConstructionAssemblyProgress);
                System.out.println("RobotThree get exchange " + exchanged);
                if (exchanged + detailConstructionAssemblyProgress >= 100) {

                    Factory factory = Factory.getInstance();
                    factory.setDetailConstructionCount(factory.getDetailConstructionCount() + 1);
                    detailConstructionAssemblyProgress = 0;
                    Thread robotFour = new RobotFour();
                    robotFour.start();
                    System.out.println("RobotThree: overall progress reached 100! set progress to 0, \n" +
                            "add 1 detailConstruction to Factory(Total: " + factory.getDetailConstructionCount() + "), start RobotFour");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("RobotThree disabled");
        interrupt();
    }


}
