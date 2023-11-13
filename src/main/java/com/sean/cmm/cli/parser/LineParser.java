package com.sean.cmm.cli.parser;

import com.sean.cmm.cli.cmd.BaseCmd;

import java.util.Map;

public class LineParser {
    private final Map<String, BaseCmd> cmdMap;

    public LineParser(Map<String, BaseCmd> cmdMap) {
        this.cmdMap = cmdMap;
    }

    public void parse(String userInput){
        cmdMap.get(userInput).execute(userInput);
    }
}
