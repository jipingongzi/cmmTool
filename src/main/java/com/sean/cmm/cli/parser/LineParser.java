package com.sean.cmm.cli.parser;

import com.sean.cmm.cli.ICmdService;
import com.sean.cmm.cli.cmd.BaseCmd;
import com.sean.cmm.cli.eventsource.CmdAppender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LineParser {
    private final static Map<String, BaseCmd> cliMap = new HashMap<>();


    public static void parse(String input, ICmdService cmdService) {
        String[] items = input.split(" ");
        String prefix = items[0];
        Object[] args = new Object[items.length - 1];
        System.arraycopy(items, 1, args, 0, items.length - 1);
        try {
            cliMap.get(prefix).execute(input);
            CmdAppender.trace(input);
        } catch (Exception e){
            CmdAppender.error();
            throw e;
        }
    }
}
