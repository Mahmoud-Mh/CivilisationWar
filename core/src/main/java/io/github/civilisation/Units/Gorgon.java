package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Gorgon extends Unit {
    private Animation<TextureRegion> deathAnimation;
    private boolean isDying = false;
    private boolean readyToRemove = false;
    private float stateTime;

    public Gorgon(float x, float y) {
        super(x, y, 300, 80, 20, UnitType.TANK); // 300 HP, 80 Attack Damage, 20 Speed

        // Initialize animations
        this.walkAnimation = AnimationFactory.create("assets/pictures/gorgon/Gorgon_1/Walk.png", 128, 13, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/gorgon/Gorgon_1/Attack_2.png", 128, 7, 0.2f);
        this.deathAnimation = AnimationFactory.create("assets/pictures/gorgon/Gorgon_1/Dead.png", 128, 3, 0.15f);

        this.stateTime = 0f;
        this.setFacingRight(true); // Gorgon moves to the right by default
    }

    @Override
    public void move() {
        if (isDying) return; // No movement if the Gorgon is dying

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
                readyToRemove = true; // Mark Gorgon for removal
            }
        } else if (isFighting) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        }

        // Flip the frame if needed based on the direction the Gorgon is facing
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
