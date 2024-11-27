package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Samurai extends Unit {

    public Samurai(float x, float y) {
        super(x, y, 100, 30, 50, UnitType.MELEE);


        this.walkAnimation = AnimationFactory.create("pictures/samurai/Samurai/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("pictures/samurai/Samurai/Attack_1.png", 73, 3, 0.2f);


        this.setFacingRight(false);
    }

    @Override
    public void move() {
        float previousX = this.x;
        this.x -= speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();


        if (this.x < 0) {
            this.x = 0;
        }


        if (this.x == previousX) {
            this.isFighting = false;
        }
    }


    public boolean isCollidingWith(Unit other) {
        return Math.abs(this.x - other.x) < 50;
    }

    public void fight(Unit other) {
        if (this.isAlive() && other.isAlive()) {
            other.takeDamage(this.attackDamage);
            this.takeDamage(other.attackDamage);
        }
    }


}
