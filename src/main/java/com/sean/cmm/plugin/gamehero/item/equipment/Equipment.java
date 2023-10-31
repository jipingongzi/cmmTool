package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.ManType;

import java.util.Set;

public abstract class Equipment {
    protected abstract String getName();
    protected abstract Set<ManType> getManTypes();

    protected abstract int increaseAttackPower();
    protected abstract int increaseDefensePower();
    protected abstract int increaseHealthPower();
    protected abstract int increaseMagicPower();
    protected abstract Set<String> skills();

}
