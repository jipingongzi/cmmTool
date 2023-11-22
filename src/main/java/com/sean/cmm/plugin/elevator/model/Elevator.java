package com.sean.cmm.plugin.elevator.model;

import com.sean.cmm.plugin.elevator.config.Config;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator implements Runnable {
    private final PriorityQueue<Integer> upTasks;
    private final PriorityQueue<Integer> downTasks;
    private final LinkedBlockingQueue<Integer> newTasks;
    private int currentFloor;

    public Elevator(int startingFloor) {
        upTasks = new PriorityQueue<>();
        downTasks = new PriorityQueue<>(Comparator.reverseOrder());
        this.newTasks = new LinkedBlockingQueue<>();
        this.currentFloor = startingFloor;
    }

    public void addTask(int floor) {
        try {
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

    @Override
    public void run() {
        while (true) {
            try {
                while (!newTasks.isEmpty()) {
                    int nextTask = newTasks.poll();
                    if (nextTask > currentFloor) {
                        upTasks.offer(nextTask);
                    } else if (nextTask < currentFloor) {
                        downTasks.offer(nextTask);
                    }
                }

                boolean isMovingUp = !upTasks.isEmpty();
                boolean isMovingDown = !isMovingUp && !downTasks.isEmpty();

                if (isMovingUp) {
                    currentFloor = upTasks.poll();
                    System.out.println("Elevator moving up, now at floor: " + currentFloor);
                    Thread.sleep(Config.getSpeed());
                }

                if (isMovingDown) {
                    currentFloor = downTasks.poll();
                    System.out.println("Elevator moving down, now at floor: " + currentFloor);
                    Thread.sleep(Config.getSpeed());
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
}