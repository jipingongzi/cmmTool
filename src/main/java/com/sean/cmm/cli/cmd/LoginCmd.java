package com.sean.cmm.cli.cmd;

import com.sean.cmm.cli.BaseCmd;

import java.util.Arrays;
import java.util.List;

public class LoginCmd extends BaseCmd {
    @Override
    public Object[] parseArgs(String userInput) {
        String[] argStrs = userInput.split(" ");
        String userName = argStrs[0];
        String pwd = argStrs[1];
        return new Object[]{userName, pwd};
    }
}
