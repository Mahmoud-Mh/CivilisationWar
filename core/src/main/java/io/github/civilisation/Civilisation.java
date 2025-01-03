package io.github.civilisation;

import io.github.civilisation.Units.Unit;
import io.github.civilisation.Units.Knight;
import io.github.civilisation.Units.UnitFactory;

import java.util.List;
import java.util.ArrayList;

public class Civilisation extends GameWorld {
    protected String name;
    protected int level;
    protected int xp;
    protected int xpRequired;
    protected int gold;
    protected List<Unit> units;
    protected Unit specialUnit;
    protected List<Turret> turrets;
    protected int turretSlots;
    protected int hpBase;

    public Civilisation(String name) {
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.xpRequired = 5000;
        this.gold = 0;
        this.units = new ArrayList<>();
        this.turrets = new ArrayList<>();
        this.turretSlots = 1;
        this.hpBase = 5000;
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
        }
    }

    public void deployUnits() {

    }

    public void useUniqueAbility(List<Unit> enemyUnits) {
        for (Unit enemy : enemyUnits) {
            if (enemy.isAlive()) {
                enemy.takeDamage(500);
            }
        }
    }

    public void levelUp() {
        if (this.xp >= this.xpRequired) {
            this.level += 1;
            this.turretSlots += 1;
            this.hpBase += 2000;
            this.xp = 0;
            this.xpRequired += 2000;
        }
        if (this.level == 3) {
            this.hpBase += 4000;
        }
    }

    public Unit buyUnit(String unitType, int cost) {
        if (gold >= cost) {
            Unit unit = UnitFactory.createUnit(unitType, 100, 10, 50);
            if (unit != null) {
                units.add(unit);
                gold -= cost;
                return unit;
            } else {
                System.out.println("Unit type not recognized.");
                return null;
            }
        } else {
            System.out.println("Not enough gold.");
            return null;
        }
    }

    public Turret buyTurret(String turretName, int attack, int cost) {
        if (gold >= cost && turrets.size() < turretSlots) {
            Turret turret = new Turret(turretName, attack, cost);
            turrets.add(turret);
            gold -= cost;
            return turret;
        } else {
            System.out.println("Not enough gold or turret slots available.");
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public int getLevel() {
        return level;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }
}
