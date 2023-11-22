package com.sean.cmm.plugin.elevator;

import com.sean.cmm.plugin.elevator.config.Config;
import com.sean.cmm.plugin.elevator.model.Elevator;

public class ElevatorApplication {
    public static void main(String[] args) throws InterruptedException {
        Elevator elevator = new Elevator(Config.getInitFloor());
        Thread elevatorThread = new Thread(elevator);
        elevatorThread.start();

        elevator.addTask(3);
        elevator.addTask(5);
        elevator.addTask(2);
        elevator.addTask(4);
        elevator.addTask(2);

        Thread.sleep(2000);
        elevator.addTask(5);

        Thread.sleep(13000);
        elevator.addTask(10);
    }
}
