package com.sean.cmm.plugin.gamehero.service;

import com.sean.cmm.plugin.gamehero.item.Character;

public interface ICharacterService {

    void init(int number);

    void choose(int index);

    void pickEquipment(int index);

    void unpickEquipment(int index);

    Character status();

    void levelUp();
}
