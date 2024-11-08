package io.github.civilisation;
import io.github.civilisation.Units.Unit;

import java.util.List;
import java.util.ArrayList;

public class Civilisation extends Game{
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
    }

    public void AddGold(int amount) {
        if (amount>0){
            this.gold += amount;
        }
    }

    public void deployUnits(Unit  unit){
        int cost = unit.getCost();
        if(gold >= cost){
            Unit unit =
        }
    }
    public void useUniqueAbility(){


    }

    public void levelUp(){
        if(this.xp>=xpRequired){
            this.level +=1;
            this.turretSlots+=1;
            this.hpBase += 2000;

        } else if(this.level== 3) {
            this.hpBase +=4000;
        }

    }

    public void buyUnit(String unitType){
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }



    public void addUnit(Unit unit) {
        units.add(unit);
    }
}
