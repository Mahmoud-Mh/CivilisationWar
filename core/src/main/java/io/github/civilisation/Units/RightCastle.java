package io.github.civilisation.Units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RightCastle {
    private float x, y, width, height;
    private int health;
    private Texture texture;
    private boolean destroyed;
    private float collisionOffsetX; // Offset for adjusting collision area

    public RightCastle(float x, float y, float width, float height, int health, String texturePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.texture = new Texture(texturePath);
        this.destroyed = false;

        // Adjust the collision area for the right castle
        this.collisionOffsetX = 80; // Positive value to shift collision point to the right
    }

    public boolean isCollidingWith(Unit unit) {
        // Adjust collision logic using collisionOffsetX
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
