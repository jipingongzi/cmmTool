package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;

import java.util.Collections;
import java.util.Set;

public class Sword extends Equipment {
    public Sword(String name) {
        super(name);
    }

    @Override
    public Set<CharacterType> getManTypes() {
        return Collections.emptySet();
    }

    @Override
    public int increaseAttackPower() {
        return 5;
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
        return 0;
    }

    @Override
    public Set<String> skills() {
        return Collections.emptySet();
    }
}
