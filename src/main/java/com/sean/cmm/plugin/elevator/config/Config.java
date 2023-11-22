package com.sean.cmm.plugin.elevator.config;

public class Config {
    /**
     * mill seconds
     */
    private final static Integer speed = 1000;
    private final static Integer initFloor = 1;

    public static Integer getSpeed() {
        return speed;
    }

    public static Integer getInitFloor() {
        return initFloor;
    }
}
