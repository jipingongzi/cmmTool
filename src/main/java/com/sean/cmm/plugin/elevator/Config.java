package com.sean.cmm.plugin.elevator;

public class Config {
    /**
     * seconds
     */
    private final static Integer speed = 3;
    /**
     * seconds
     */
    private final static Integer stopTime = 3;
    private final static Integer highestFloor = 10;
    private final static Integer initFloor = 10;

    public static Integer getSpeed() {
        return speed;
    }

    public static Integer getStopTime() {
        return stopTime;
    }

    public static Integer getHighestFloor() {
        return highestFloor;
    }

    public static Integer getInitFloor() {
        return initFloor;
    }
}
