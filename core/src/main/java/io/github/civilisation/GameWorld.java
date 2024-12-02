package io.github.civilisation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.civilisation.Units.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld implements com.badlogic.gdx.InputProcessor {
    private SpriteBatch batch;
    private List<Unit> alliedUnits;
    private List<Unit> enemyUnits;
    private float elapsedTime;
    private Texture backgroundTexture;
    private Texture leftsideTexture;
    private Texture rightsideTexture;
    private float commonY; // Common Y position for all units
    private float leftCastleX, rightCastleX; // Positions for castles
    private float castleY; // Adjusted Y position for castles

    public GameWorld() {
        batch = new SpriteBatch();
        alliedUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        elapsedTime = 0f;

        // Common Y position for all units
        commonY = 120;

        // Adjusted castle Y position
        castleY = 100; // Lowered the Y position of castles

        // Fixed castle positions
        leftCastleX = -80;
        rightCastleX = 580;

        // Initial Units
        addAlliedUnit(new Knight(0, commonY)); // Spawns in front of the left castle
        addEnemyUnit(new Samurai(660, commonY)); // Spawns in front of the right castle

        // Background and castle textures
        backgroundTexture = new Texture("pictures/bg/Game.jpg");
        leftsideTexture = new Texture("pictures/castle/0.png");
        rightsideTexture = new Texture("fpictures/castle/0.png");

        // Register input processor for keyboard input
        com.badlogic.gdx.Gdx.input.setInputProcessor(this);
    }

    public void addAlliedUnit(Unit unit) {
        alliedUnits.add(unit);
    }

    public void addEnemyUnit(Unit unit) {
        enemyUnits.add(unit);
    }

    public void updateAndRender() {
        elapsedTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        float screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        float screenHeight = com.badlogic.gdx.Gdx.graphics.getHeight();

        float castleWidth = 300;
        float castleHeight = 300;

        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 10, screenWidth, screenHeight);

        // Draw castles with the adjusted Y position
        batch.draw(leftsideTexture, leftCastleX, castleY, castleWidth, castleHeight);
        batch.draw(rightsideTexture, rightCastleX, castleY, castleWidth, castleHeight);

        // Update and draw allied and enemy units
        updateAndDrawUnits(alliedUnits, enemyUnits);
        updateAndDrawUnits(enemyUnits, alliedUnits);

        batch.end();
    }

    private void updateAndDrawUnits(List<Unit> units, List<Unit> enemyUnits) {
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();

            if (unit instanceof Samurai && ((Samurai) unit).isReadyToRemove()) {
                iterator.remove();
                continue;
            }

            if (!unit.isAlive() && !(unit instanceof Samurai)) {
                iterator.remove();
                continue;
            }

            boolean isFighting = false;

            for (Unit enemy : enemyUnits) {
                if (enemy.isAlive() && unit.isCollidingWith(enemy)) {
                    unit.fight(enemy);

                    if (!enemy.isAlive()) {
                        enemyUnits.remove(enemy);
                    }
                    if (!unit.isAlive() && !(unit instanceof Samurai)) {
                        iterator.remove();
                    }

                    isFighting = true;
                    break;
                }
            }

            if (!isFighting) {
                unit.move();
            }

            unit.updateAndDraw(batch, elapsedTime, enemyUnits);
        }
    }

    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        for (Unit unit : alliedUnits) {
            unit.dispose();
        }
        for (Unit unit : enemyUnits) {
            unit.dispose();
        }
        leftsideTexture.dispose();
        rightsideTexture.dispose();
        backgroundTexture.dispose();
    }

    public void endGame(String winner) {
        System.out.println("Game Over! Winner: " + winner);
        com.badlogic.gdx.Gdx.app.exit();
    }

    // InputProcessor implementations

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            // Allied Units (Left Side)
            case com.badlogic.gdx.Input.Keys.NUM_1: // Knight (Melee)
                addAlliedUnit(new Knight(0, commonY)); // Spawns near the left castle
                break;
            case com.badlogic.gdx.Input.Keys.NUM_2: // Wizard (Ranged)
                addAlliedUnit(new Wizard(0, commonY)); // Spawns near the left castle
                break;

            // Enemy Units (Right Side)
            case com.badlogic.gdx.Input.Keys.C: // Samurai (Melee)
                addEnemyUnit(new Samurai(660, commonY)); // Spawns near the right castle
                break;
            case com.badlogic.gdx.Input.Keys.S: // Skeleton (Ranged)
                addEnemyUnit(new Skeleton(660, commonY)); // Spawns near the right castle
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false; // Empty implementation
    }
}
