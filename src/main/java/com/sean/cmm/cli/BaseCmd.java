package com.sean.cmm.cli;

public abstract class BaseCmd implements ICmd{

    protected ICmdService service;
    public BaseCmd getCmd(String userInput){
        return null;
    }
    public abstract Object[] parseArgs(String userInput);
    @Override
    public void execute(String userInput) {
        BaseCmd cmd = getCmd(userInput);
        Object[] args = parseArgs(userInput);
        cmd.service.execute(args);
    }

}
