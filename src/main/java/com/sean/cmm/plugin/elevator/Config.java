package com.sean.cmm.plugin.elevator;

public class Config {
    /**
     * seconds
     */
    private final Integer speed = 3;
    /**
     * seconds
     */
    private final Integer stopTime = 3;
    private final Integer highestFloor = 10;
    private final Integer initFloor = 10;

    public Integer getSpeed() {
        return speed;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public Integer getHighestFloor() {
        return highestFloor;
    }

    public Integer getInitFloor() {
        return initFloor;
    }
}
