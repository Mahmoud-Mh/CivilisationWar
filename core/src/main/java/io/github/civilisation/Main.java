package io.github.civilisation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture knightTexture, knightFightingTexture, samuraiTexture, samuraiFightingTexture;
    private Animation<TextureRegion> walkAnimationKnight, fightAnimationKnight, walkAnimationEnemy, fightAnimationEnemy;
    private List<Unit> deployedUnits;
    private float elapsedTime;
    private float commonY;

    @Override
    public void create() {
        batch = new SpriteBatch();
        deployedUnits = new ArrayList<>();

        // Initialisation des textures et animations du chevalier et du samouraï
        knightTexture = new Texture("pictures/Knight/Knight_1/Walk.png");
        knightFightingTexture = new Texture("pictures/Knight/Knight_1/Attack 1.png");
        walkAnimationKnight = createAnimation(knightTexture, 73, 5, 0.1f);
        fightAnimationKnight = createAnimation(knightFightingTexture, 73, knightFightingTexture.getWidth() / 73, 0.2f);

        samuraiTexture = new Texture("pictures/samurai/Samurai/Walk.png");
        samuraiFightingTexture = new Texture("pictures/samurai/Samurai/Attack_1.png");
        walkAnimationEnemy = createAnimation(samuraiTexture, 73, 5, 0.1f, true);
        fightAnimationEnemy = createAnimation(samuraiFightingTexture, 73, samuraiFightingTexture.getWidth() / 73, 0.2f, true);

        commonY = 100;
        elapsedTime = 0f;

        // Déploiement initial de deux unités
        deployUnit("knight", 140, commonY); // Chevalier à gauche
        deployUnit("samurai", 600, commonY); // Samouraï à droite
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int frameWidth, int frameCount, float frameDuration) {
        return createAnimation(texture, frameWidth, frameCount, frameDuration, false);
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int frameWidth, int frameCount, float frameDuration, boolean flipX) {
        TextureRegion[][] frames = TextureRegion.split(texture, frameWidth, texture.getHeight());
        TextureRegion[] animationFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            animationFrames[i] = frames[0][i];
            if (flipX && !animationFrames[i].isFlipX()) {
                animationFrames[i].flip(true, false);
            }
        }
        return new Animation<>(frameDuration, animationFrames);
    }

    public void deployUnit(String type, float startX, float startY) {
        Unit unit;
        if (type.equalsIgnoreCase("knight")) {
            unit = new Unit(startX, startY, walkAnimationKnight, fightAnimationKnight, true);
        } else if (type.equalsIgnoreCase("samurai")) {
            unit = new Unit(startX, startY, walkAnimationEnemy, fightAnimationEnemy, false);
        } else {
            System.out.println("Type d'unité non reconnu : " + type);
            return;
        }
        deployedUnits.add(unit);
        System.out.println("Unité déployée : " + type + " à la position X : " + startX);
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        for (Unit unit : deployedUnits) {
            unit.updateAndDraw(batch, elapsedTime, deployedUnits);
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

class Unit {
    private float x, y;
    private Animation<TextureRegion> walkAnimation, fightAnimation;
    private boolean isFighting;
    private boolean isKnight;

    public Unit(float startX, float startY, Animation<TextureRegion> walkAnimation, Animation<TextureRegion> fightAnimation, boolean isKnight) {
        this.x = startX;
        this.y = startY;
        this.walkAnimation = walkAnimation;
        this.fightAnimation = fightAnimation;
        this.isFighting = false;
        this.isKnight = isKnight;
    }

    public void updateAndDraw(SpriteBatch batch, float elapsedTime, List<Unit> units) {
        // Cherche une autre unité pour engager le combat si proche
        for (Unit other : units) {
            if (other != this && !this.isFighting && !other.isFighting && isCloseTo(other)) {
                this.startFighting();
                other.startFighting();
            }
        }

        // Déplace l'unité si elle n'est pas encore en mode combat
        if (!isFighting) {
            moveTowards(isKnight ? 50 : -50); // Chevalier à droite, Samouraï à gauche
        }

        // Affiche l'unité avec l'animation appropriée
        TextureRegion currentFrame = isFighting ? fightAnimation.getKeyFrame(elapsedTime, true) : walkAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, x, y);
    }

    private void moveTowards(float speed) {
        x += speed * Gdx.graphics.getDeltaTime();
    }

    private boolean isCloseTo(Unit other) {
        return Math.abs(this.x - other.x) < 50; // Distance pour commencer le combat
    }

    public void startFighting() {
        isFighting = true;
    }
}
