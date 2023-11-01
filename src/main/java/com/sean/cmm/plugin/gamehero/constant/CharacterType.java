package com.sean.cmm.plugin.gamehero.constant;

public enum CharacterType {
    WARRIOR(25, 6, 100, 0,
            5, 2, 10, 0),
    MAGE(10, 2, 50, 40,
            3, 1, 5, 8),
    SHOOTER(15, 4, 60, 0,
            4, 3, 7, 0);

    CharacterType(Integer attackPower, Integer defensePower, Integer heathPower, Integer magicPower,
                  Integer attackPower4LevelUp, Integer defensePower4LevelUp, Integer heathPower4LevelUp, Integer magicPower4LevelUp) {
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.heathPower = heathPower;
        this.magicPower = magicPower;

        this.attackPower4LevelUp = attackPower4LevelUp;
        this.defensePower4LevelUp = defensePower4LevelUp;
        this.heathPower4LevelUp = heathPower4LevelUp;
        this.magicPower4LevelUp = magicPower4LevelUp;
    }

    private final Integer attackPower;
    private final Integer defensePower;
    private final Integer heathPower;
    private final Integer magicPower;

    private final Integer attackPower4LevelUp;
    private final Integer defensePower4LevelUp;
    private final Integer heathPower4LevelUp;
    private final Integer magicPower4LevelUp;

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

    public Integer getAttackPower4LevelUp() {
        return attackPower4LevelUp;
    }

    public Integer getDefensePower4LevelUp() {
        return defensePower4LevelUp;
    }

    public Integer getHeathPower4LevelUp() {
        return heathPower4LevelUp;
    }

    public Integer getMagicPower4LevelUp() {
        return magicPower4LevelUp;
    }
}
