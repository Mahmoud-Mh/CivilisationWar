package io.github.civilisation.Units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LeftCastle {
    private float x, y, width, height;
    private int health;
    private Texture texture;
    private boolean destroyed;
    private float collisionOffsetX;

    public LeftCastle(float x, float y, float width, float height, int health, String texturePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.texture = new Texture(texturePath);
        this.destroyed = false;


        this.collisionOffsetX = -80;
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
                destroyed = true;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (!destroyed) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void dispose() {
        texture.dispose();
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
