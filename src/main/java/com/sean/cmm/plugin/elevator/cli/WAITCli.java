package com.sean.cmm.plugin.elevator.cli;

import com.sean.cmm.plugin.elevator.model.Elevator;

public class WAITCli extends BaseCli{
    @Override
    protected int argSize() {
        return 1;
    }

    @Override
    protected void cliExecute(Elevator elevator, Object[] args) {
        try {
            Thread.sleep(Integer.parseInt(args[0].toString()) * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
