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
    protected boolean isIdle = false;
    protected UnitType unitType;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Texture walkTexture;
    protected Texture attackTexture;

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


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public boolean isAlive() {
        return this.health > 0;
    }

    public boolean isFighting() {
        return isFighting;
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }


    public void attack(Unit target) {
        target.takeDamage(this.attackDamage);
    }

    public void takeDamage(int amount) {
        if (isAlive()) {
            this.health -= amount;
            if (this.health <= 0) {
                this.health = 0;
                die();
            }
        }
    }

    protected void die() {
        this.isFighting = false;

    }


    public void move() {
        if (isIdle) {
            return;
        }
        this.x += facingRight ? speed : -speed;
    }

    public boolean isCollidingWith(Unit other) {
        return Math.abs(this.x - other.x) < 50;
    }

    public boolean isCollidingWith(float otherX, float otherY, float otherWidth, float otherHeight) {
        return this.x < otherX + otherWidth && this.x + 50 > otherX &&
            this.y < otherY + otherHeight && this.y + 50 > otherY;
    }

    public void fight(Unit other) {
        if (this.isCollidingWith(other) && this.isAlive() && other.isAlive()) {
            this.isFighting = true;
            other.isFighting = true;
            this.attack(other);
            other.attack(this);

            if (!this.isAlive()) this.isFighting = false;
            if (!other.isAlive()) other.isFighting = false;
        }
    }


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


    public void dispose() {
        if (walkTexture != null) {
            walkTexture.dispose();
        }
        if (attackTexture != null) {
            attackTexture.dispose();
        }
    }

    public void setIdle(boolean idle) {
        this.isIdle = idle;
        if (idle) {
            this.speed = 0;
        }
    }

    public boolean isIdle() {
        return this.isIdle;
    }


    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}
