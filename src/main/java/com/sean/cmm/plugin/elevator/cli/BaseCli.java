package com.sean.cmm.plugin.elevator.cli;


import com.sean.cmm.plugin.elevator.model.Elevator;

public abstract class BaseCli implements ICli {

    protected boolean validArgs(Object[] args) {
        if (argSize() == 0) {
            return true;
        } else if (args == null || args.length == 0) {
            System.out.println("No required args");
            return false;
        } else if (argSize() > args.length) {
            System.out.println("Invalid args");
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return min arg numbers
     */
    protected abstract int argSize();

    protected abstract void cliExecute(Elevator elevator, Object[] args);

    @Override
    public void execute(Elevator elevator, Object[] args) {
        if (validArgs(args)) {
            cliExecute(elevator, args);
        }
    }
}
