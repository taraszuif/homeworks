package me.zuif.hw2.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SummerThread extends Thread {
    private final boolean medium;
    private final List<Integer> list;
    private final int listMedium;
    AtomicBoolean stopped = new AtomicBoolean();
    AtomicInteger sum = new AtomicInteger();

    public SummerThread(boolean medium, List<Integer> ints) {
        this.medium = medium;
        this.list = ints;
        listMedium = list.size() / 2;
    }

    @Override
    public void run() {
        stopped.set(false);
        int sum = 0;
        if (medium) {
            for (int i = listMedium; i < list.size();
                 i++) {
                sum += list.get(i);
            }
        } else {
            for (int i = 0; i < listMedium; i++) {
                sum += list.get(i);
            }
        }
        this.sum.set(sum);
        System.out.println("Thread " + Thread.currentThread().getName() + " sum: " + sum);
        stopped.set(true);
    }

    public AtomicBoolean getStopped() {
        return stopped;
    }

    public int getSum() {
        return sum.get();
    }
}
