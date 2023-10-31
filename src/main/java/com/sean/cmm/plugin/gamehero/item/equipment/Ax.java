package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.ManType;

import java.util.Collections;
import java.util.Set;

public class Ax extends Equipment {

    public Ax(String name) {
        super(name);
    }

    @Override
    protected Set<ManType> getManTypes() {
        return Collections.singleton(ManType.WARRIOR);
    }

    @Override
    protected int increaseAttackPower() {
        return 10;
    }

    @Override
    protected int increaseDefensePower() {
        return 0;
    }

    @Override
    protected int increaseHealthPower() {
        return 0;
    }

    @Override
    protected int increaseMagicPower() {
        return 0;
    }

    @Override
    protected Set<String> skills() {
        return Collections.emptySet();
    }
}
