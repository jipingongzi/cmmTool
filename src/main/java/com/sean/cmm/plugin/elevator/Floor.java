package com.sean.cmm.plugin.elevator;

public class Floor {
    private Integer index;
    private Boolean triggered;

    public Floor(Integer index) {
        this.index = index;
        this.triggered = false;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getTriggered() {
        return triggered;
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }
}
