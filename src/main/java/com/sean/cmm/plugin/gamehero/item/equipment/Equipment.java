package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;

import java.util.Set;

public abstract class Equipment {
    protected String name;
    public abstract Set<CharacterType> getManTypes();

    public abstract int increaseAttackPower();
    public abstract int increaseDefensePower();
    public abstract int increaseHealthPower();
    public abstract int increaseMagicPower();
    public abstract Set<String> skills();

    public Equipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
