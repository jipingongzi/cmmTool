package com.sean.cmm.plugin.gamehero.item.equipment;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;

import java.util.Collections;
import java.util.Set;

public class Ax extends Equipment {

    public Ax(String name) {
        super(name);
    }

    @Override
    public Set<CharacterType> getManTypes() {
        return Collections.singleton(CharacterType.WARRIOR);
    }

    @Override
    public int increaseAttackPower() {
        return 10;
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
