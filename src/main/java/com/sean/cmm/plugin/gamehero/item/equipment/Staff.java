package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.ManType;

import java.util.Collections;
import java.util.Set;

public class Staff extends Equipment {
    public Staff(String name) {
        super(name);
    }

    @Override
    protected Set<ManType> getManTypes() {
        return Collections.singleton(ManType.MAGE);
    }

    @Override
    protected int increaseAttackPower() {
        return 2;
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
        return 10;
    }

    @Override
    protected Set<String> skills() {
        return Collections.singleton("Fire ball");
    }
}
