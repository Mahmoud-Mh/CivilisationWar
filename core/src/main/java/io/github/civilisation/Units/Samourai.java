package io.github.civilisation.Units;

public class Samourai extends Unit{
    public Samourai(int health, int attackdamage, UnitType unitType) {
        super(100, attackdamage, unitType.MELEE);
    }
}
