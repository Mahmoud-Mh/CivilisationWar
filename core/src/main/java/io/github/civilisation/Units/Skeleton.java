package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Skeleton extends Unit {
    private Animation<TextureRegion> deathAnimation;
    private boolean isDying = false;
    private boolean readyToRemove = false;
    private float stateTime;

    public Skeleton(float x, float y) {
        super(x, y, 70, 20, 40, UnitType.RANGED); // 70 HP, 20 Attack Damage, 40 Speed

        // Initialize animations
        this.walkAnimation = AnimationFactory.create("assets/pictures/skeleton/Skeleton1/Walk.png", 128, 7, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/skeleton/Skeleton1/Attack_3.png", 128, 4, 0.2f);
        this.deathAnimation = AnimationFactory.create("assets/pictures/skeleton/Skeleton1/Dead.png", 128, 4, 0.15f);

        this.stateTime = 0f;
        this.setFacingRight(false); // Skeleton faces left by default
    }

    @Override
    public void move() {
        if (isDying) return; // No movement if the Skeleton is dying

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
                readyToRemove = true; // Mark Skeleton for removal
            }
        } else if (isFighting) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        // Flip the frame if needed based on the direction the Skeleton is facing
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
