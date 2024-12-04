package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Knight extends Unit {
    private Animation<TextureRegion> deathAnimation;
    private boolean isDying = false;
    private boolean readyToRemove = false;
    private float stateTime;

    public Knight(float x, float y) {
        super(x, y, 80, 20, 40, UnitType.MELEE); // 800 HP, 50 attack damage, 40 speed

        // Initialize animations
        this.walkAnimation = AnimationFactory.create("pictures/Knight/Knight_1/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("pictures/Knight/Knight_1/Attack 3.png", 73, 4, 0.2f);
        this.deathAnimation = AnimationFactory.create("pictures/Knight/Knight_1/Dead.png", 73, 5, 0.15f);

        this.stateTime = 0f;
        this.setFacingRight(true); // Knight typically moves right by default
    }

    @Override
    public void move() {
        if (isDying) return; // No movement if the Knight is dying

        float previousX = this.x; // Store the position before moving
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Move to the right

        // Prevent the unit from going off-screen
        if (this.x > com.badlogic.gdx.Gdx.graphics.getWidth() - 50) { // Adjust 50 based on unit width
            this.x = com.badlogic.gdx.Gdx.graphics.getWidth() - 50;
        }

        // If the unit didn't move, stop walking animation
        if (this.x == previousX) {
            this.isFighting = false; // Stop fighting if the unit is stuck
        }
    }

    @Override
    public void updateAndDraw(SpriteBatch batch, float elapsedTime, List<Unit> enemyUnits) {
        stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        // Reset fighting state
        isFighting = false;

        // Check for collisions with enemies
        for (Unit enemy : enemyUnits) {
            if (enemy.isAlive() && this.isCollidingWith(enemy)) {
                isFighting = true;
                break;
            }
        }

        // Determine the current animation frame based on the state
        TextureRegion currentFrame;
        if (isDying) {
            currentFrame = deathAnimation.getKeyFrame(stateTime, false);
            if (deathAnimation.isAnimationFinished(stateTime)) {
                readyToRemove = true;
            }
        } else if (isFighting) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        // Flip the frame if needed based on the direction the Knight is facing
        if (!facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        // Draw the current frame
        batch.draw(currentFrame, x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public boolean isReadyToRemove() {
        return readyToRemove;
    }

    @Override
    protected void die() {
        isDying = true;
        stateTime = 0f; // Reset animation timer for death animation
    }
}
