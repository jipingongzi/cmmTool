package com.sean.cmm.plugin.gamehero.item;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;
import com.sean.cmm.plugin.gamehero.item.equipment.Equipment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Character {
    private static final int maxEquipmentNumber = 6;
    private static final int maxLevelNumber = 10;

    private final String name;
    private final CharacterType type;
    private Integer level;
    private Integer attackPower;
    private Integer defensePower;
    private Integer heathPower;
    private Integer magicPower;

    private final List<Equipment> equipments;
    private final Set<String> skill;

    public Character(String name, CharacterType manType) {
        this.name = name;
        this.type = manType;
        this.level = 1;
        this.attackPower = manType.getAttackPower();
        this.defensePower = manType.getDefensePower();
        this.heathPower = manType.getHeathPower();
        this.magicPower = manType.getMagicPower();
        this.equipments = new ArrayList<>();
        this.skill = new LinkedHashSet<>();
    }

    public void pick(Equipment equipment) {
        if (this.equipments.size() >= 6) {
            System.out.printf("You can only pick %d equipments \n", maxEquipmentNumber);
            return;
        }
        equipments.add(equipment);
    }

    public void unPick(Integer index) {
        Equipment equipment = this.equipments.get(index);
        if (equipment != null) {
            this.equipments.remove(equipment);
            System.out.printf("You unpicked %s \n", equipment.getName());
        }
    }

    public void levelUp(){
        if(level >= maxLevelNumber){
            System.out.printf("Max level is %d \n", maxLevelNumber);
            return;
        }
        this.level++;
        this.attackPower += this.type.getAttackPower4LevelUp();
        this.defensePower += this.type.getDefensePower4LevelUp();
        this.heathPower += this.type.getHeathPower4LevelUp();
        this.magicPower += this.type.getMagicPower4LevelUp();
    }

    @Override
    public String toString() {
        StringBuilder attackPowerStr = new StringBuilder(this.attackPower.toString());
        StringBuilder defensePowerStr = new StringBuilder(this.defensePower.toString());
        StringBuilder healthPowerStr = new StringBuilder(this.heathPower.toString());
        StringBuilder magicPowerStr = new StringBuilder(this.magicPower.toString());
        Set<String> allSkills = new LinkedHashSet<>(this.skill);
        for (Equipment equipment : this.equipments) {
            if (equipment.increaseAttackPower() > 0) {
                attackPowerStr.append(" + ").append(equipment.increaseAttackPower());
            }
            if (equipment.increaseDefensePower() > 0) {
                defensePowerStr.append(" + ").append(equipment.increaseDefensePower());
            }
            if (equipment.increaseHealthPower() > 0) {
                healthPowerStr.append(" + ").append(equipment.increaseHealthPower());
            }
            if (equipment.increaseMagicPower() > 0) {
                magicPowerStr.append(" + ").append(equipment.increaseMagicPower());
            }
            allSkills.addAll(equipment.skills());
        }

        String baseInfo = this.name + ", " + this.type.name().toLowerCase()
                + ", " + attackPowerStr + ", " + defensePowerStr + ", "
                + healthPowerStr + ", " + magicPowerStr + "\n";
        String skillStr = "Skill: [" + String.join(",", allSkills) + "] \n";
        String equipmentStr = "Equipments: ["
                + this.equipments.stream().map(Equipment::getName).collect(Collectors.joining(","))
                + "] \n";
        return baseInfo + skillStr + equipmentStr;
    }
}
