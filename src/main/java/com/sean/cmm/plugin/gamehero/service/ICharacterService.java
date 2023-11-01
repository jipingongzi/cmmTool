package com.sean.cmm.plugin.gamehero.service;

public interface ICharacterService {

    void init(Integer number);

    void pickEquipment(Integer index);

    void unpickEquipment(Integer index);

    void status();

    void levelUp();
}
