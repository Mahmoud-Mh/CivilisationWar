package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Yokai extends Unit {
    private Animation<TextureRegion> deathAnimation;
    private boolean isDying = false;
    private boolean readyToRemove = false;
    private float stateTime;

    public Yokai(float x, float y) {
        super(x, y, 250, 70, 25, UnitType.SPECIAL); // 250 HP, 70 Attack Damage, 25 Speed


        this.walkAnimation = AnimationFactory.create("pictures/yokai/Karasu/Walk.png", 128, 8, 0.1f);
        this.attackAnimation = AnimationFactory.create("pictures/yokai/Karasu/Attack_3.png", 128, 3, 0.2f);
        this.deathAnimation = AnimationFactory.create("pictures/yokai/Karasu/Dead.png", 128, 6, 0.15f);

        this.stateTime = 0f;
        this.setFacingRight(false);
    }

    @Override
    public void move() {
        if (isDying) return;

        this.x -= speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Move to the left
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
