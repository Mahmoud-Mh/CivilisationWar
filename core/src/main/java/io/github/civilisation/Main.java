package io.github.civilisation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture knightTexture;
    private Texture knightFightingTexture;
    private Texture samuraiTexture;
    private Texture samuraiFightingTexture;
    private Animation<TextureRegion> walkAnimationKnight;
    private Animation<TextureRegion> fightAnimationKnight;
    private Animation<TextureRegion> walkAnimationEnemy;
    private Animation<TextureRegion> fightAnimationEnemy;
    private float elapsedTime;
    private float knightX;
    private float enemyX;
    private float commonY;
    private boolean isFighting;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Initialize knight walking animation
        knightTexture = new Texture("pictures/Knight/Knight_1/Walk.png");
        TextureRegion[][] knightFrames = TextureRegion.split(knightTexture, 73, knightTexture.getHeight()); // Adjust as necessary
        int knightFrameCount = Math.min(5, knightFrames[0].length);
        TextureRegion[] walkFramesKnight = new TextureRegion[knightFrameCount];
        for (int i = 0; i < knightFrameCount; i++) {
            walkFramesKnight[i] = knightFrames[0][i];
        }
        walkAnimationKnight = new Animation<>(0.1f, walkFramesKnight);
        walkAnimationKnight.setPlayMode(Animation.PlayMode.LOOP);

        // Initialize knight fighting animation
        knightFightingTexture = new Texture("pictures/Knight/Knight_1/Attack 1.png");
        TextureRegion[][] knightFightFrames = TextureRegion.split(knightFightingTexture, 73, knightFightingTexture.getHeight());
        int fightFrameCountKnight = Math.min(knightFightFrames[0].length, knightFightFrames[0].length);
        TextureRegion[] fightFramesKnight = new TextureRegion[fightFrameCountKnight];
        for (int i = 0; i < fightFrameCountKnight; i++) {
            fightFramesKnight[i] = knightFightFrames[0][i];
        }
        fightAnimationKnight = new Animation<>(0.2f, fightFramesKnight); // Slow down for testing
        fightAnimationKnight.setPlayMode(Animation.PlayMode.LOOP);

        knightX = 140;

        // Initialize enemy walking animation
        samuraiTexture = new Texture("pictures/samurai/Samurai/Walk.png");
        TextureRegion[][] enemyFrames = TextureRegion.split(samuraiTexture, 73, samuraiTexture.getHeight()); // Adjust frame size
        int enemyFrameCount = Math.min(5, enemyFrames[0].length);
        TextureRegion[] walkFramesEnemy = new TextureRegion[enemyFrameCount];
        for (int i = 0; i < enemyFrameCount; i++) {
            walkFramesEnemy[i] = enemyFrames[0][i];
            if (!walkFramesEnemy[i].isFlipX()) { // Prevent multiple flips
                walkFramesEnemy[i].flip(true, false); // Flip to face left only once
            }
        }
        walkAnimationEnemy = new Animation<>(0.1f, walkFramesEnemy);
        walkAnimationEnemy.setPlayMode(Animation.PlayMode.LOOP);

        // Initialize enemy fighting animation
        samuraiFightingTexture = new Texture("pictures/samurai/Samurai/Attack_1.png");
        TextureRegion[][] enemyFightFrames = TextureRegion.split(samuraiFightingTexture, 73, samuraiFightingTexture.getHeight());
        TextureRegion[] fightFramesEnemy = new TextureRegion[enemyFightFrames[0].length];
        for (int i = 0; i < enemyFightFrames[0].length; i++) {
            fightFramesEnemy[i] = enemyFightFrames[0][i];
            if (!fightFramesEnemy[i].isFlipX()) { // Prevent multiple flips
                fightFramesEnemy[i].flip(true, false); // Ensure facing left only once
            }
        }
        fightAnimationEnemy = new Animation<>(0.2f, fightFramesEnemy); // Slow down for testing
        fightAnimationEnemy.setPlayMode(Animation.PlayMode.LOOP);

        enemyX = 800 - enemyFrames[0][0].getRegionWidth(); // Start from the right edge

        commonY = 100; // Common y-coordinate for both characters
        elapsedTime = 0f;
        isFighting = false; // Initially, they are not fighting
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Update positions and check if the characters should start fighting
        if (!isFighting) {
            float knightSpeed = 100 * Gdx.graphics.getDeltaTime();
            float enemySpeed = 100 * Gdx.graphics.getDeltaTime();

            // Move knight to the right
            if (knightX < enemyX - walkAnimationKnight.getKeyFrame(elapsedTime, true).getRegionWidth()) {
                knightX += knightSpeed;
            } else {
                isFighting = true; // Start fighting when they meet
                elapsedTime = 0; // Reset elapsed time for fighting animation
            }

            // Move enemy to the left
            if (enemyX > knightX + walkAnimationEnemy.getKeyFrame(elapsedTime, true).getRegionWidth()) {
                enemyX -= enemySpeed;
            }
        }

        batch.begin();
        TextureRegion currentFrameKnight;
        TextureRegion currentFrameEnemy;

        // Choose the animation frame based on the state
        if (isFighting) {
            currentFrameKnight = fightAnimationKnight.getKeyFrame(elapsedTime, true);
            currentFrameEnemy = fightAnimationEnemy.getKeyFrame(elapsedTime, true);

            // Adjust position if fighting frames are wider
            float knightAdjustX = currentFrameKnight.getRegionWidth() / 2f;
            float enemyAdjustX = currentFrameEnemy.getRegionWidth() / 2f;

            batch.draw(currentFrameKnight, knightX - knightAdjustX, commonY);
            batch.draw(currentFrameEnemy, enemyX - enemyAdjustX, commonY);
        } else {
            currentFrameKnight = walkAnimationKnight.getKeyFrame(elapsedTime, true);
            currentFrameEnemy = walkAnimationEnemy.getKeyFrame(elapsedTime, true);
            batch.draw(currentFrameKnight, knightX, commonY);
            batch.draw(currentFrameEnemy, enemyX, commonY);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        knightTexture.dispose();
        knightFightingTexture.dispose();
        samuraiTexture.dispose();
        samuraiFightingTexture.dispose();
    }
}
