package io.github.civilisation.Units;

public abstract class Unit {
    protected int cost;
    public int health;
    protected int attackdamage;
    protected UnitType unitType;
    protected String name;
    protected int xp;

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
        this.xp = 0;
    }

    public void attack(Unit target) {
        if (target != null && target.isAlive()) {
            target.takeDamage(attackdamage);
            gainXp(10);
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;

    }

    public void heal(int amount) {
        health += amount;

    }

    private void gainXp(int amount) {
        xp += amount;
        if (xp >= 100) {
            levelUp();
            xp = 0;
        }
    }

    private void levelUp() {
        attackdamage += 5;
        health += 10;

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
