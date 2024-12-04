package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class WereWolf extends Unit {
    private Animation<TextureRegion> deathAnimation;
    private boolean isDying = false;
    private boolean readyToRemove = false;
    private float stateTime;

    public WereWolf(float x, float y) {
        super(x, y, 300, 100, 40, UnitType.TANK); // 300 HP, 100 Attack Damage, 40 Speed

        // Initialize animations
        this.walkAnimation = AnimationFactory.create("assets/pictures/wereWolf/black/walk.png", 128, 11, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/wereWolf/black/Attack_2.png", 128, 4, 0.2f);
        this.deathAnimation = AnimationFactory.create("assets/pictures/wereWolf/black/Dead.png", 128, 2, 0.15f);

        this.stateTime = 0f;
        this.setFacingRight(false); // Facing left by default
    }

    @Override
    public void move() {
        if (isDying) return; // No movement if the WereWolf is dying

        this.x -= speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Move to the left
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
                readyToRemove = true; // Mark WereWolf for removal
            }
        } else if (isFighting) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        // Flip the frame if needed based on the direction the WereWolf is facing
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
