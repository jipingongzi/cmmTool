package com.sean.cmm.plugin.gamehero.constant;

public enum ManType {
    WARRIOR(25, 6, 100, 0),
    MAGE(10, 2, 50, 40),
    SHOOTER(15, 4, 60 , 0);

    ManType(Integer attackPower, Integer defensePower, Integer heathPower, Integer magicPower) {
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.heathPower = heathPower;
        this.magicPower = magicPower;
    }

    private final Integer attackPower;
    private final Integer defensePower;
    private final Integer heathPower;
    private final Integer magicPower;

    public Integer getAttackPower() {
        return attackPower;
    }

    public Integer getDefensePower() {
        return defensePower;
    }

    public Integer getHeathPower() {
        return heathPower;
    }

    public Integer getMagicPower() {
        return magicPower;
    }
}
