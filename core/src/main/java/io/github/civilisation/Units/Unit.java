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

    // Getters and setters for position
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

    // Getters for health and fighting status
    public boolean isAlive() {
        return this.health > 0;
    }

    public boolean isFighting() {
        return isFighting;
    }

    // Combat mechanics
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
        // Override for specific unit death behavior if needed
    }

    // Movement logic
    public void move() {
        this.x += facingRight ? speed : -speed;
    }

    public boolean isCollidingWith(Unit other) {
        return Math.abs(this.x - other.x) < 50; // Adjust threshold as needed
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

    // Animation and rendering
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

    // Cleanup
    public void dispose() {
        if (walkTexture != null) {
            walkTexture.dispose();
        }
        if (attackTexture != null) {
            attackTexture.dispose();
        }
    }

    // Utility
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}
