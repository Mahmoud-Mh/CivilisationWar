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
    private LeftCastle leftCastle;
    private RightCastle rightCastle;
    private float commonY; // Common Y position for all units
    private float castleY; // Adjusted Y position for castles

    public GameWorld() {
        batch = new SpriteBatch();
        alliedUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        elapsedTime = 1f;

        // Common Y position for all units
        commonY = 120;

        // Adjusted castle Y position
        castleY = 100;

        // Initialize castles
        leftCastle = new LeftCastle(-80, castleY, 300, 300, 10000, "pictures/castle/0.png");
        rightCastle = new RightCastle(580, castleY, 300, 300, 10000, "pictures/castle/0.png");

        // Background texture
        backgroundTexture = new Texture("pictures/bg/Game.jpg");

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

        batch.begin();

        // Draw background
        batch.draw(backgroundTexture, 0, 10, screenWidth, screenHeight);

        // Draw castles
        leftCastle.draw(batch);
        rightCastle.draw(batch);

        // Update and draw allied and enemy units
        updateAndDrawUnits(alliedUnits, rightCastle);
        updateAndDrawUnits(enemyUnits, leftCastle);

        batch.end();
    }

    private void resolveUnitCollision(Unit unit1, Unit unit2) {
        float overlap = 50 - Math.abs(unit1.getX() - unit2.getX()); // Assuming a collision threshold of 50
        if (overlap > 0) {
            if (unit1.getX() < unit2.getX()) {
                unit1.setX(unit1.getX() - overlap / 2);
                unit2.setX(unit2.getX() + overlap / 2);
            } else {
                unit1.setX(unit1.getX() + overlap / 2);
                unit2.setX(unit2.getX() - overlap / 2);
            }
        }
    }

    private void updateAndDrawUnits(List<Unit> units, Object targetCastle) {
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();

            // Resolve collisions with other units in the same list
            for (Unit otherUnit : units) {
                if (unit != otherUnit && unit.isCollidingWith(otherUnit)) {
                    resolveUnitCollision(unit, otherUnit);
                }
            }

            // Check collision with the target castle
            if (targetCastle instanceof LeftCastle && ((LeftCastle) targetCastle).isCollidingWith(unit)) {
                unit.setIdle(true); // Stop the unit
                ((LeftCastle) targetCastle).takeDamage(unit.getAttackDamage()); // Damage the castle
                if (((LeftCastle) targetCastle).isDestroyed()) {
                    endGame("Enemies");
                }
                unit.updateAndDraw(batch, elapsedTime, new ArrayList<>()); // Play idle animation
                continue;
            }

            if (targetCastle instanceof RightCastle && ((RightCastle) targetCastle).isCollidingWith(unit)) {
                unit.setIdle(true); // Stop the unit
                ((RightCastle) targetCastle).takeDamage(unit.getAttackDamage()); // Damage the castle
                if (((RightCastle) targetCastle).isDestroyed()) {
                    endGame("Allies");
                }
                unit.updateAndDraw(batch, elapsedTime, new ArrayList<>()); // Play idle animation
                continue;
            }

            if (!unit.isAlive()) {
                iterator.remove();
                continue;
            }

            boolean isFighting = false;

            // Check collision with enemy units
            for (Unit enemy : (units == alliedUnits ? enemyUnits : alliedUnits)) {
                if (enemy.isAlive() && unit.isCollidingWith(enemy)) {
                    unit.fight(enemy);

                    if (!enemy.isAlive()) {
                        (units == alliedUnits ? enemyUnits : alliedUnits).remove(enemy);
                    }
                    if (!unit.isAlive()) {
                        iterator.remove();
                    }

                    isFighting = true;
                    break;
                }
            }

            if (!isFighting) {
                unit.move();
            }

            unit.updateAndDraw(batch, elapsedTime, units == alliedUnits ? enemyUnits : alliedUnits);
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
        leftCastle.dispose();
        rightCastle.dispose();
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
                addAlliedUnit(new Knight(0, commonY)); // Spawn Knight only when commanded
                break;
            case com.badlogic.gdx.Input.Keys.NUM_2: // Wizard (Ranged)
                addAlliedUnit(new Wizard(0, commonY)); // Spawn Wizard only when commanded
                break;
            case com.badlogic.gdx.Input.Keys.NUM_3: // Gorgon (Tank)
                addAlliedUnit(new Gorgon(0, commonY)); // Spawn Gorgon only when commanded
                break;
            case com.badlogic.gdx.Input.Keys.NUM_4: // Drake (Special)
                addAlliedUnit(new Drake(0, commonY)); // Spawn Drake only when commanded
                break;

            // Enemy Units (Right Side)
            case com.badlogic.gdx.Input.Keys.U: // Samurai (Melee)
                addEnemyUnit(new Samurai(660, commonY)); // Spawn Samurai only when commanded
                break;
            case com.badlogic.gdx.Input.Keys.I: // Skeleton (Ranged)
                addEnemyUnit(new Skeleton(660, commonY)); // Spawn Skeleton only when commanded
                break;

            case com.badlogic.gdx.Input.Keys.O: // Yokai (Ranged)
                addEnemyUnit(new Yokai(660, commonY)); // Spawn Skeleton only when commanded
                break;

            case com.badlogic.gdx.Input.Keys.P: // Werewolf (Ranged)
                addEnemyUnit(new WereWolf(660, commonY)); // Spawn Skeleton only when commanded
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
