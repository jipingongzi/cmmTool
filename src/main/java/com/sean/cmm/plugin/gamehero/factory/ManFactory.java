package com.sean.cmm.plugin.gamehero.factory;

import com.sean.cmm.plugin.gamehero.constant.ManType;
import com.sean.cmm.plugin.gamehero.item.Man;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ManFactory {

    private static final List<Man> warriorPool = new ArrayList<>();
    private static final List<Man> magePool = new ArrayList<>();
    private static final List<Man> shooterPool = new ArrayList<>();
    static {
        warriorPool.add(new Man("BM", ManType.WARRIOR));
        warriorPool.add(new Man("DK", ManType.WARRIOR));
        warriorPool.add(new Man("DH", ManType.WARRIOR));
        warriorPool.add(new Man("TC", ManType.WARRIOR));

        warriorPool.add(new Man("DL", ManType.MAGE));
        warriorPool.add(new Man("AM", ManType.MAGE));
        warriorPool.add(new Man("PAL", ManType.MAGE));
        warriorPool.add(new Man("KOG", ManType.MAGE));

        warriorPool.add(new Man("BMG", ManType.SHOOTER));
        warriorPool.add(new Man("MK", ManType.SHOOTER));
        warriorPool.add(new Man("FS", ManType.SHOOTER));
        warriorPool.add(new Man("HL", ManType.SHOOTER));
    }

    public static List<Man> init(Integer number){
        List<Man> slots = new ArrayList<>();
        Random random = new Random();
        List<List<Man>> pool = Arrays.asList(warriorPool, magePool, shooterPool);
        if(number > pool.size()){
            System.out.printf("Currently only support %d characters", pool.size());
            number = pool.size();
        }
        for (int i = 0; i < number; i++) {
            List<Man> currentList = pool.get(random.nextInt(pool.size()));
            if (!currentList.isEmpty()) {
                int elementIndex = random.nextInt(currentList.size());
                Man man = currentList.remove(elementIndex);
                slots.add(man);
            }
            if (currentList.isEmpty()) {
                pool.remove(currentList);
            }
        }
        return slots;
    }
}
