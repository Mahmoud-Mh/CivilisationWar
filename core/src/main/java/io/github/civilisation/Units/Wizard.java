package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Wizard extends Unit {
    private Animation<TextureRegion> deathAnimation;
    private boolean isDying = false;
    private boolean readyToRemove = false;
    private float stateTime;

    public Wizard(float x, float y) {
        super(x, y, 60, 30, 30, UnitType.RANGED); // 70 HP, 50 Attack Damage, 30 Speed

        // Initialize animations
        this.walkAnimation = AnimationFactory.create("assets/pictures/wizard/Fire wizard/Walk.png", 128, 6, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/wizard/Fire wizard/Attack_1.png", 128, 4, 0.2f);
        this.deathAnimation = AnimationFactory.create("assets/pictures/wizard/Fire wizard/Dead.png", 128, 6, 0.15f);

        this.stateTime = 0f;
        this.setFacingRight(true); // Wizard faces right by default
    }

    @Override
    public void move() {
        if (isDying) return; // No movement if the Wizard is dying

        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Move to the right
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
                readyToRemove = true; // Mark Wizard for removal
            }
        } else if (isFighting) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        // Flip the frame if needed based on the direction the Wizard is facing
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
