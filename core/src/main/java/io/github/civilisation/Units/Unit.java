package io.github.civilisation.Units;

public abstract class Unit {
    protected int health;
    protected int attackdmg;
    protected String type;
    protected int cost;


    public Unit( int health, int attackdmg,String type, int cost) {
        this.health = health;
        this.attackdmg = attackdmg;
        this.type = type;
        this.cost = cost;
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





