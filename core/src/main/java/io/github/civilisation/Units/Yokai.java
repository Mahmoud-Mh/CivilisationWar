package io.github.civilisation.Units;

public class Yokai extends Unit{
    public Yokai(int health, int attackdamage, UnitType unitType) {
        super(250, attackdamage, unitType.SPECIAL);
    }
}
