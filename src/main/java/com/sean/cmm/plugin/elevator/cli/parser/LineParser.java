package com.sean.cmm.plugin.elevator.cli.parser;


import com.sean.cmm.cli.eventsource.CmdAppender;
import com.sean.cmm.plugin.elevator.cli.BaseCli;
import com.sean.cmm.plugin.elevator.cli.GoCli;
import com.sean.cmm.plugin.elevator.cli.WAITCli;
import com.sean.cmm.plugin.elevator.constant.CliEnum;
import com.sean.cmm.plugin.elevator.model.Elevator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LineParser {
    private static final Map<CliEnum, BaseCli> cliMap = new HashMap<>();

    static {
        cliMap.put(CliEnum.GO, new GoCli());
        cliMap.put(CliEnum.WAIT, new WAITCli());
    }

    public static void parse(String input, Elevator elevator) {
        String[] items = input.split(" ");
        String prefix = items[0];
        Optional<CliEnum> cliOptional = CliEnum.findByText(prefix);
        if (!cliOptional.isPresent()) {
            System.out.println("No such cmd");
            return;
        }
        CliEnum cli = cliOptional.get();
        Object[] args = new Object[items.length - 1];
        System.arraycopy(items, 1, args, 0, items.length - 1);
        try {
            cliMap.get(cli).execute(elevator, args);
            CmdAppender.trace(input);
        } catch (Exception e){
            CmdAppender.error();
            throw e;
        }
    }
}
