package com.sean.cmm.plugin.elevator.constant;

import java.util.Optional;

public enum CliEnum {

    GO("go"),
    WAIT("wait");

    private final String text;

    CliEnum(String text) {
        this.text = text;
    }

    public static Optional<CliEnum> findByText(String text){
        CliEnum[] cmdEnums = CliEnum.values();
        for (CliEnum cmdEnum : cmdEnums) {
            if (cmdEnum.text.equals(text)) {
                return Optional.of(cmdEnum);
            }
        }
        return Optional.empty();
    }
}
