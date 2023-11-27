package com.sean.cmm.plugin.elevator.model;

import com.sean.cmm.plugin.elevator.config.Config;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Elevator implements Runnable {
    private static final AtomicInteger count = new AtomicInteger(1);
    private Integer id;
    private final PriorityBlockingQueue<Integer> upTasks;
    private final PriorityBlockingQueue<Integer> downTasks;
    private final LinkedBlockingQueue<Integer> newTasks;
    private volatile Boolean waitTime;
    private int currentFloor;

    public Elevator(int startingFloor) {
        id = count.getAndAdd(1);
        upTasks = new PriorityBlockingQueue<>();
        downTasks = new PriorityBlockingQueue<>(10, Comparator.reverseOrder());
        newTasks = new LinkedBlockingQueue<>();
        currentFloor = startingFloor;
        waitTime = false;
    }

    public void addWait() {
        waitTime = true;
    }

    public void addTask(int floor) {
        try {
            if (waitTime) {
                // wait all task finished
                while (!newTasks.isEmpty() || !upTasks.isEmpty() || !downTasks.isEmpty()) {
                    Thread.sleep(1000L);
                }
                waitTime = false;
            }
            if (currentFloor != floor &&
                    !newTasks.contains(floor) &&
                    !upTasks.contains(floor) &&
                    !downTasks.contains(floor)) {
                newTasks.put(floor);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void listen(){
        while (true){
            if(!newTasks.isEmpty()){
                int nextTask = newTasks.poll();
                if (nextTask > currentFloor) {
                    upTasks.offer(nextTask);
                } else if (nextTask < currentFloor) {
                    downTasks.offer(nextTask);
                }
            }
        }
    }

    @Override
    public void run() {
        CompletableFuture.runAsync(this::listen);
        while (true) {
            try {
                boolean isMovingUp = !upTasks.isEmpty();
                boolean isMovingDown = !isMovingUp && !downTasks.isEmpty();
                if (isMovingUp) {
                    int distance = upTasks.peek() - currentFloor;
                    for (int i = 0; i < distance; i++) {
                        Thread.sleep(Config.getSpeed());
                        currentFloor++;
                    }
                    upTasks.poll();
                    System.out.println("Elevator " + id + " moving up, now at floor: " + currentFloor);
                }
                if (isMovingDown) {
                    int distance = currentFloor - downTasks.peek();
                    for (int i = 0; i < distance; i++) {
                        Thread.sleep(Config.getSpeed());
                        currentFloor--;
                    }
                    downTasks.poll();
                    System.out.println("Elevator " + id + " moving down, now at floor: " + currentFloor);
                }
                while (isMovingUp && !upTasks.isEmpty() && upTasks.peek() < currentFloor) {
                    newTasks.offer(upTasks.poll());
                }
                while (isMovingDown && !downTasks.isEmpty() && downTasks.peek() > currentFloor) {
                    newTasks.offer(downTasks.poll());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getId() {
        return id;
    }

}