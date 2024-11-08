package io.github.civilisation;

public class Unit {
    protected String name;
    protected int health;
    protected int attackdmg;


    public Unit(String name, int health, int attackdmg) {
        this.name = name;
        this.health = health;
        this.attackdmg = attackdmg;
    }

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

    public void attack(Unit target){
        if(target != null && target.health>0){
            target.health -= attackdmg;

        } else {
            System.out.println("déja mort ");
        }
    }


    public int getAttackdmg() {
        return attackdmg;
    }

    public int getHealth() {
        return health;
    }
}





