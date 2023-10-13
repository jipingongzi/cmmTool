package com.sean.cmm.cli;

public abstract class BaseCmd implements ICmd{
    public BaseCmd getCmd(String userInput){
        return null;
    }
    abstract protected Object[] parseArgs(String userInput);
    @Override
    public void execute(String userInput) {
        BaseCmd cmd = getCmd(userInput);
        Object[] args = parseArgs(userInput);

    }

}
