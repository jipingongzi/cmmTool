package com.sean.cmm.plugin.gamehero.factory;

import com.sean.cmm.plugin.gamehero.constant.CharacterType;
import com.sean.cmm.plugin.gamehero.item.Character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CharacterFactory {

    private static final List<Character> warriorPool = new ArrayList<>();
    private static final List<Character> magePool = new ArrayList<>();
    private static final List<Character> shooterPool = new ArrayList<>();
    static {
        warriorPool.add(new Character("BM", CharacterType.WARRIOR));
        warriorPool.add(new Character("DK", CharacterType.WARRIOR));
        warriorPool.add(new Character("DH", CharacterType.WARRIOR));
        warriorPool.add(new Character("TC", CharacterType.WARRIOR));

        warriorPool.add(new Character("DL", CharacterType.MAGE));
        warriorPool.add(new Character("AM", CharacterType.MAGE));
        warriorPool.add(new Character("PAL", CharacterType.MAGE));
        warriorPool.add(new Character("KOG", CharacterType.MAGE));

        warriorPool.add(new Character("BMG", CharacterType.SHOOTER));
        warriorPool.add(new Character("MK", CharacterType.SHOOTER));
        warriorPool.add(new Character("FS", CharacterType.SHOOTER));
        warriorPool.add(new Character("HL", CharacterType.SHOOTER));
    }

    public static List<Character> init(Integer number){
        List<Character> slots = new ArrayList<>();
        Random random = new Random();
        List<List<Character>> pool = Arrays.asList(warriorPool, magePool, shooterPool);
        if(number > pool.size()){
            System.out.printf("Currently only support %d characters", pool.size());
            number = pool.size();
        }
        for (int i = 0; i < number; i++) {
            List<Character> currentList = pool.get(random.nextInt(pool.size()));
            if (!currentList.isEmpty()) {
                int elementIndex = random.nextInt(currentList.size());
                Character character = currentList.remove(elementIndex);
                slots.add(character);
            }
            if (currentList.isEmpty()) {
                pool.remove(currentList);
            }
        }
        return slots;
    }
}
