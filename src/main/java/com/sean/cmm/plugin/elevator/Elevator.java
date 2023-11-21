package com.sean.cmm.plugin.elevator;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator implements Runnable {
    private PriorityQueue<Integer> upTasks;
    private PriorityQueue<Integer> downTasks;
    private LinkedBlockingQueue<Integer> newTasks;
    private int currentFloor;
    private boolean isMovingUp;
    private boolean isMovingDown;

    public Elevator(int startingFloor) {
        upTasks = new PriorityQueue<>();
        downTasks = new PriorityQueue<>(Comparator.reverseOrder());
        this.newTasks = new LinkedBlockingQueue<>();
        this.currentFloor = startingFloor;
    }

    public void addTask(int floor) {
        try {
            if (!newTasks.contains(floor)) {
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

                isMovingUp = !upTasks.isEmpty();
                isMovingDown = !isMovingUp && !downTasks.isEmpty();

                if (isMovingUp) {
                    currentFloor = upTasks.poll();
                    System.out.println("Elevator moving up, now at floor: " + currentFloor);
                    Thread.sleep(1000); // 模拟运行耗时
                }

                if (isMovingDown) {
                    currentFloor = downTasks.poll();
                    System.out.println("Elevator moving down, now at floor: " + currentFloor);
                    Thread.sleep(1000); // 模拟运行耗时
                }

                // 检查来自同一方向的任务
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
    public static void main(String[] args) throws InterruptedException {
        Elevator elevator = new Elevator(1);
        Thread elevatorThread = new Thread(elevator);
        elevatorThread.start(); // 开始电梯线程

        elevator.addTask(3);
        elevator.addTask(5);
        elevator.addTask(2);
        elevator.addTask(4);
        // 需要时继续添加更多任务
        elevator.addTask(2);
        Thread.sleep(3000);
        elevator.addTask(2);
        Thread.sleep(13000);
        elevator.addTask(10);
    }
}