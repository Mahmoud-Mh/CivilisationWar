package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Samurai extends Unit {
    private Animation<TextureRegion> deathAnimation; // Death animation
    private boolean isDying = false; // Tracks if the Samurai is dying
    private boolean readyToRemove = false; // Tracks if the Samurai is ready to be removed
    private float stateTime; // Tracks elapsed time for animations

    public Samurai(float x, float y) {
        super(x, y, 100, 30, 50, UnitType.MELEE);

        // Initialize animations
        this.walkAnimation = AnimationFactory.create("pictures/samurai/Samurai/Walk.png", 128, 9, 0.1f);
        this.attackAnimation = AnimationFactory.create("pictures/samurai/Samurai/Attack_1.png", 73, 4, 0.2f);
        this.deathAnimation = AnimationFactory.create("pictures/samurai/Samurai/Dead.png", 128, 6, 0.15f);

        this.stateTime = 0f; // Initialize animation state time
        this.setFacingRight(false);
    }

    @Override
    public void move() {
        if (isDying) return; // Prevent movement if dying

        float previousX = this.x;
        this.x -= speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        if (this.x < 0) {
            this.x = 0;
        }

        if (this.x != previousX) {
            this.isFighting = false;
        }
    }

    @Override
    public void fight(Unit other) {
        if (this.isAlive() && other.isAlive()) {
            other.takeDamage(this.attackDamage);
            this.takeDamage(other.attackDamage);

            // Check if the Samurai is dead
            if (!this.isAlive() && !isDying) {
                isDying = true;
                this.stateTime = 0f; // Reset state time for death animation
            }
        }
    }

    @Override
    public void updateAndDraw(SpriteBatch batch, float elapsedTime, List<Unit> enemyUnits) {
        stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame;
        if (isDying) {
            currentFrame = deathAnimation.getKeyFrame(stateTime, false); // Play death animation once
            if (deathAnimation.isAnimationFinished(stateTime)) {
                readyToRemove = true; // Mark for removal after animation
            }
        } else if (isFighting) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true); // Loop attack animation
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true); // Loop walk animation
        }

        if (!facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        // Draw the current frame, including the death frame
        batch.draw(currentFrame, x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    // Getter to check if the Samurai is ready for removal
    public boolean isReadyToRemove() {
        return readyToRemove;
    }
}
