package io.github.civilisation.Units;

public class Drake extends Unit{
    public Drake(int health, int attackdamage, UnitType unitType) {
        super(200 , attackdamage, unitType.SPECIAL);
    }
}
