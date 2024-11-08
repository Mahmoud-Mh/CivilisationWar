package io.github.civilisation.Units;

public class Wizard extends Unit{

    public Wizard(int health, int attackdamage, UnitType unitType) {
        super(70, attackdamage, unitType.RANGED);
    }
}
