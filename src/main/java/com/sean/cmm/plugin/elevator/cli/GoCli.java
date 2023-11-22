package com.sean.cmm.plugin.elevator.cli;

import com.sean.cmm.plugin.elevator.model.Elevator;

public class GoCli extends BaseCli{
    @Override
    protected int argSize() {
        return 1;
    }

    @Override
    protected void cliExecute(Elevator elevator, Object[] args) {
        Integer floor = Integer.parseInt(args[0].toString());
    }
}
