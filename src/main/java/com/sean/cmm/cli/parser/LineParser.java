package com.sean.cmm.cli.parser;

import com.sean.cmm.cli.cmd.BaseCmd;

import java.util.Map;

public class LineParser {
    private final Map<String, BaseCmd> cliMap;

    public LineParser(Map<String, BaseCmd> cliMap) {
        this.cliMap = cliMap;
    }

    public void parse(String userInput){
        cliMap.get(userInput).execute(userInput);
    }
}
