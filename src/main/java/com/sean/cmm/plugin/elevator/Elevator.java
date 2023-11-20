package com.sean.cmm.plugin.elevator;

import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Elevator {
    private final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    private final StatusEnum status = StatusEnum.STOPPED;

    public void click(Integer floor){
        if(queue.contains(floor)){
            return;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
        for (int i = 0; i < 100; i++) {
            int n = i;
            CompletableFuture.runAsync(() -> {

                System.out.println("Offer: " + n + "  " + queue.offer(n));
            });
        }
        Thread.sleep(10000);
        System.out.println(queue.size());
        System.out.println("--------------");

        int l = queue.size();
        for (int i = 0; i < l; i++) {
            System.out.println("take: " + queue.take());
        }

    }
}
