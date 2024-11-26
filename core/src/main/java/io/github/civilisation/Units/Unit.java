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
    protected boolean facingRight; // Orientation de l'unité
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
        this.facingRight = true; // Par défaut, orienté vers la droite
    }

    public abstract void move();

    public void updateAndDraw(SpriteBatch batch, float elapsedTime, List<Unit> enemyUnits) {
        TextureRegion currentFrame = isFighting
            ? attackAnimation.getKeyFrame(elapsedTime, true)
            : walkAnimation.getKeyFrame(elapsedTime, true);

        // Gérer l'orientation en fonction de "facingRight"
        if (!facingRight) {
            batch.draw(currentFrame, x + currentFrame.getRegionWidth(), y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        } else {
            batch.draw(currentFrame, x, y);
        }
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0; // Empêche les points de vie négatifs
        }
    }

    public boolean isAlive() {
        return this.health > 0;
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

    // Vérifie si cette unité est en collision avec une autre unité
    public boolean isCollidingWith(Unit other) {
        // Vérifie si la distance horizontale est inférieure à une marge
        return Math.abs(this.x - other.x) < 50; // Ajuster la marge selon les besoins
    }

    // Gère le combat entre cette unité et une autre
    public void fight(Unit other) {
        if (this.isAlive() && other.isAlive()) {
            // Les unités s'infligent des dégâts mutuellement
            other.takeDamage(this.attackDamage);
            this.takeDamage(other.attackDamage);

            // Les unités passent en mode combat
            this.isFighting = true;
            other.isFighting = true;
        }
    }

    // Arrête le combat si l'unité ou l'adversaire est mort
    public void stopFighting(Unit other) {
        if (!this.isAlive() || !other.isAlive()) {
            this.isFighting = false;
            other.isFighting = false;
        }
    }
}
