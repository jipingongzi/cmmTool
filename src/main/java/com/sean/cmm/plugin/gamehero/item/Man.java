package com.sean.cmm.plugin.gamehero.item;

import com.sean.cmm.plugin.gamehero.constant.ManType;
import com.sean.cmm.plugin.gamehero.item.equipment.Equipment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Man {
    private static final int maxEquipmentNumber = 6;
    private static final int maxLevelNumber = 10;

    private String name;
    private ManType type;
    private Integer attackPower;
    private Integer defensePower;
    private Integer heathPower;
    private Integer magicPower;

    private List<Equipment> equipments;
    private Set<String> skill;

    public Man(String name, ManType manType) {
        this.name = name;
        this.type = manType;
        this.attackPower = manType.getAttackPower();
        this.defensePower = manType.getDefensePower();
        this.heathPower = manType.getHeathPower();
        this.magicPower = manType.getMagicPower();
        this.equipments = new ArrayList<>();
        this.skill = new LinkedHashSet<>();
    }

    public void pick(Equipment equipment){
        if(this.equipments.size() >= 6){
            System.out.printf("You can only pick %d equipments%n", maxEquipmentNumber);
            return;
        }
        equipments.add(equipment);
    }

    public void unPick(Integer index){
        this.equipments.remove(index.intValue());
    }


}
