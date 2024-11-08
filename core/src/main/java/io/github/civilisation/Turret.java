package io.github.civilisation;

public class Turret extends Civilisation {
    private int attack;
    private int cost;

    public Turret(String name, int attack, int cost) {
        super(name);
        this.attack = attack;
        this.cost = cost;
    }

    public int getAttack() {
        return attack;
    }

    public int getCost() {
        return cost;
    }

    public void operate() {
    }
}
