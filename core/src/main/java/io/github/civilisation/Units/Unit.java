package io.github.civilisation.Units;

public abstract class Unit {
    protected int cost;
    public int health;
    protected int attackdamage;
    protected UnitType unitType;
    protected String name;

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

    public Unit(String name, int health, int attackdamage, UnitType unitType) {
        this.name = name;
        this.health = health;
        this.attackdamage = attackdamage;
        this.unitType = unitType;
        this.cost = unitType.getCost();
    }

    public void attack(Unit target) {
        if (target != null && target.isAlive()) {
            target.takeDamage(attackdamage);
        } else {
            System.out.println(target.getName() + " est déjà mort.");
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;

    }

    public void heal(int amount) {
        health += amount;
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

    public String getName() {
        return name;
    }
}
