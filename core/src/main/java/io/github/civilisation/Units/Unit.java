package io.github.civilisation.Units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public abstract class Unit {
    protected float x, y;
    protected float speed;
    protected int health;
    protected int attackDamage;
    protected boolean isFighting;
    protected boolean facingRight;
    protected UnitType unitType;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Texture walkTexture;
    protected Texture attackTexture;
    private float lastAttackTime = 0;

    public enum UnitType {
        MELEE, RANGED, TANK, SPECIAL
    }

    public Unit(float x, float y, int health, int attackDamage, float speed, UnitType unitType) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.unitType = unitType;
        this.isFighting = false;
        this.facingRight = true;
    }

    public abstract void move();

    public void updateAndDraw(SpriteBatch batch, float elapsedTime, List<Unit> enemyUnits) {
        if (isFighting) {
            boolean stillFighting = false;
            for (Unit enemy : enemyUnits) {
                if (this.isCollidingWith(enemy) && enemy.isAlive()) {
                    stillFighting = true;
                    break;
                }
            }
            if (!stillFighting) {
                isFighting = false;
            }
        }

        TextureRegion currentFrame = isFighting
            ? attackAnimation.getKeyFrame(elapsedTime, true)
            : walkAnimation.getKeyFrame(elapsedTime, true);

        if (!facingRight) {
            batch.draw(currentFrame, x + currentFrame.getRegionWidth(), y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        } else {
            batch.draw(currentFrame, x, y);
        }



    }


    public void takeDamage(int amount) {
        if (isFighting) {
            this.health -= amount;
        }
        if (this.health < 0) {
            this.health = 0;
        }
    }


    public boolean isAlive() {
        return this.health > 0;
    }
    public void checkCombatStatus(Unit other) {
        if (!this.isAlive() || !other.isAlive() || !this.isCollidingWith(other)) {
            this.isFighting = false;
            other.isFighting = false;
        }
    }


    public void dispose() {
        if (walkTexture != null) {
            walkTexture.dispose();
        }
        if (attackTexture != null) {
            attackTexture.dispose();
        }
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isCollidingWith(Unit other) {
        return Math.abs(this.x - other.x) < 50;
    }

    public void fight(Unit other) {
        if (this.isCollidingWith(other) && this.isAlive() && other.isAlive()) {
            this.isFighting = true;
            other.isFighting = true;

            other.takeDamage(this.attackDamage);
            this.takeDamage(other.attackDamage);
        }
    }
}
