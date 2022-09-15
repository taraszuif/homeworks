package me.zuif.hw2.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SummerThread extends Thread {
    private final List<Integer> list;
    AtomicBoolean stopped = new AtomicBoolean();
    AtomicInteger sum = new AtomicInteger();

    public SummerThread(List<Integer> ints) {
        this.list = ints;
    }

    @Override
    public void run() {
        stopped.set(false);
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
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
