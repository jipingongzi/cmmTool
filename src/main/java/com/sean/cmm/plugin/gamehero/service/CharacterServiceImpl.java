package com.sean.cmm.plugin.gamehero.service;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;
import com.sean.cmm.plugin.gamehero.factory.CharacterFactory;
import com.sean.cmm.plugin.gamehero.item.Character;
import com.sean.cmm.plugin.gamehero.item.equipment.Equipment;

import java.util.ArrayList;
import java.util.List;

public class CharacterServiceImpl implements ICharacterService {

    private List<Character> characters = new ArrayList<>();
    private Character currentCharacter = null;

    private final IEquipmentService equipmentService;

    public CharacterServiceImpl(IEquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Override
    public void init(int number) {
        characters = CharacterFactory.init(number);
    }

    @Override
    public void choose(int index) {
        if (index >= characters.size()) {
            System.out.println("Invalid parameter");
            return;
        }
        currentCharacter = characters.get(index);
        System.out.printf("Hi %s %s! Go on an adventure. \n",
                currentCharacter.getName(), currentCharacter.getType().name());
    }

    @Override
    public void pickEquipment(int index) {
        Equipment equipment = equipmentService.get(index);
        if(equipment == null){
            System.out.println("Invalid parameter");
            return;
        }
        currentCharacter.pick(equipment);
    }

    @Override
    public void unpickEquipment(int index) {
        currentCharacter.unPick(index);
    }

    @Override
    public Character status() {
        System.out.println(currentCharacter);
        return currentCharacter;
    }

    @Override
    public void levelUp() {
        currentCharacter.levelUp();
        CharacterType type = currentCharacter.getType();
        System.out.printf("Hi %s, your level is %d, " + type.getLevelUpStr() + "\n", currentCharacter.getName());
    }
}
