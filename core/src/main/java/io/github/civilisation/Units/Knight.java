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
        this.setFacingRight(true);
    }

    @Override
    public void move() {
        if (isDying) return;

        float previousX = this.x;
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();


        if (this.x > com.badlogic.gdx.Gdx.graphics.getWidth() - 50) {
            this.x = com.badlogic.gdx.Gdx.graphics.getWidth() - 50;
        }


        if (this.x == previousX) {
            this.isFighting = false;
        }
    }

    @Override
    public void updateAndDraw(SpriteBatch batch, float elapsedTime, List<Unit> enemyUnits) {
        stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();


        isFighting = false;


        for (Unit enemy : enemyUnits) {
            if (enemy.isAlive() && this.isCollidingWith(enemy)) {
                isFighting = true;
                break;
            }
        }


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


        if (!facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }


        batch.draw(currentFrame, x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public boolean isReadyToRemove() {
        return readyToRemove;
    }

    @Override
    protected void die() {
        isDying = true;
        stateTime = 0f;
    }
}
