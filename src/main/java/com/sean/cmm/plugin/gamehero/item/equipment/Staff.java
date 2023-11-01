package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;

import java.util.Collections;
import java.util.Set;

public class Staff extends Equipment {
    public Staff(String name) {
        super(name);
    }

    @Override
    public Set<CharacterType> getManTypes() {
        return Collections.singleton(CharacterType.MAGE);
    }

    @Override
    public int increaseAttackPower() {
        return 2;
    }

    @Override
    public int increaseDefensePower() {
        return 0;
    }

    @Override
    public int increaseHealthPower() {
        return 0;
    }

    @Override
    public int increaseMagicPower() {
        return 10;
    }

    @Override
    public Set<String> skills() {
        return Collections.singleton("Fire ball");
    }
}
