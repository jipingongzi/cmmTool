package com.sean.cmm.plugin.gamehero.factory;

import com.sean.cmm.plugin.gamehero.item.equipment.*;

import java.util.Arrays;
import java.util.List;

public class EquipmentFactory {

    private static final List<Equipment> pool;
    static {
        pool = Arrays.asList(new Sword("Sword"), new Ax("Ax"),
                new Gun("Gun"), new Staff("Staff"));
    }
    public static List<Equipment> init(Integer number){
        if(number > pool.size()){
            System.out.printf("Currently only support %d equipments", pool.size());
            number = pool.size();
        }
        return pool.subList(0, number);
    }
}
