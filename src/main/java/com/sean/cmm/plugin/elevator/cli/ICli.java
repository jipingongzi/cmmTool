package com.sean.cmm.plugin.elevator.cli;

import com.sean.cmm.plugin.elevator.model.Elevator;

public interface ICli {
    void execute(Elevator elevator, Object[] args);
}
