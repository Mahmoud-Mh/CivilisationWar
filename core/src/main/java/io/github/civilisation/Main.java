package io.github.civilisation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture knightTexture;
    private Animation<TextureRegion> walkAnimation;
    private float elapsedTime;
    private float knightX;

    @Override
    public void create() {
        batch = new SpriteBatch();
        knightTexture = new Texture("pictures/Knight/Knight_1/Walk.png");
        TextureRegion[][] tmpFrames = TextureRegion.split(knightTexture, 72, knightTexture.getHeight());
        TextureRegion[] walkFrames = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            walkFrames[i] = tmpFrames[0][i];
        }
        walkAnimation = new Animation<>(0.1f, walkFrames);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
        elapsedTime = 0f;
        knightX = 140;
    }


    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        TextureRegion currentFrame = walkAnimation.getKeyFrame(elapsedTime, true);
        knightX += 100 * Gdx.graphics.getDeltaTime();

        batch.draw(currentFrame, knightX, 210);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        knightTexture.dispose();
    }
}
