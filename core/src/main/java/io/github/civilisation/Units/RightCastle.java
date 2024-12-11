package io.github.civilisation.Units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RightCastle {
    private float x, y, width, height;
    private int health, maxHealth;
    private Texture texture;
    private boolean destroyed;
    private float collisionOffsetX;
    private ShapeRenderer shapeRenderer;

    public RightCastle(float x, float y, float width, float height, int health, String texturePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.maxHealth = health;
        this.texture = new Texture(texturePath);
        this.destroyed = false;
        this.collisionOffsetX = 80;
        this.shapeRenderer = new ShapeRenderer();
    }

    public boolean isCollidingWith(Unit unit) {

        return unit.getX() + 50 > x + collisionOffsetX &&
            unit.getX() < x + width + collisionOffsetX &&
            unit.getY() + 50 > y &&
            unit.getY() < y + height;
    }

    public void takeDamage(int damage) {
        if (!destroyed) {
            health -= damage;
            if (health <= 0) {
                health = 0;
                destroyed = true;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (!destroyed) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void drawHealthBar() {
        float barWidth = width; // Largeur totale de la barre de vie
        float barHeight = 10;   // Hauteur de la barre de vie
        float healthPercentage = (float) health / maxHealth;
        float currentBarWidth = barWidth * healthPercentage;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Arrière-plan (barre vide)
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(x, y - barHeight - 5, barWidth, barHeight);

        // Premier plan (santé actuelle)
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y - barHeight - 5, currentBarWidth, barHeight);

        shapeRenderer.end();
    }


    public void dispose() {
        texture.dispose();
        shapeRenderer.dispose();
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
