package io.github.civilisation.Units;

public class Skeleton extends Unit{

    public Skeleton(int health, int attackdamage, UnitType unitType) {
        super(70, attackdamage, unitType.RANGED);
    }
}
