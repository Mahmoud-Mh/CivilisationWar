package io.github.civilisation;

public class Turret {
    private int attack;
    private int cost;
    private String name;

    public Turret(String name, int attack, int cost) {
        this.name = name;
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
