package com.sean.cmm.plugin.gamehero.service;

import com.sean.cmm.plugin.gamehero.factory.EquipmentFactory;
import com.sean.cmm.plugin.gamehero.item.equipment.Equipment;

import java.util.ArrayList;
import java.util.List;

public class EquipmentServiceImpl implements IEquipmentService{
    private List<Equipment> equipments = new ArrayList<>();
    @Override
    public void init(int number) {
        equipments = EquipmentFactory.init(number);
    }

    @Override
    public Equipment get(int index) {
        if(index >= equipments.size()){
            return null;
        }
        return equipments.get(index);
    }
}
