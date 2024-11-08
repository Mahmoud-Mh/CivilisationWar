package io.github.civilisation.Units;

public abstract class Unit {
    protected int cost;
    public int health;
    protected int attackdamage;
    protected UnitType unitType;

    public enum UnitType {
        MELEE(50),
        RANGED(75),
        TANK(100),
        SPECIAL(200);

        private final int cost;

        UnitType(int cost) {
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }
    }

    public Unit(int health, int attackdamage, UnitType unitType) {
        this.health = health;
        this.attackdamage = attackdamage;
        this.unitType = unitType;
        this.cost = unitType.getCost();
    }

    public void attack(Unit target) {
        if (target != null && target.isAlive()) {
            target.health -= attackdamage;
        } else {
            System.out.println("mort");
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getAttackdamage() {
        return attackdamage;
    }

    public int getHealth() {
        return health;
    }

    public int getCost() {
        return cost;
    }
}
