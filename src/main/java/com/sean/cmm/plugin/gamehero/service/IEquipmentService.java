package com.sean.cmm.plugin.gamehero.service;

import com.sean.cmm.plugin.gamehero.item.equipment.Equipment;

public interface IEquipmentService {

    void init(int number);

    Equipment get(int index);

}
