package io.github.civilisation.Units;

public class WereWolf extends Unit{
    public WereWolf(int health, int attackdamage, UnitType unitType) {
        super(300, attackdamage, unitType.TANK);
    }
}
