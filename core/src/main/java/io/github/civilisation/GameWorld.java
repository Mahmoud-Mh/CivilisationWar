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
    private float commonY;
    private float castleY;

    public GameWorld() {
        batch = new SpriteBatch();
        alliedUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        elapsedTime = 1f;


        commonY = 120;


        castleY = 100;


        leftCastle = new LeftCastle(-80, castleY, 300, 300, 10000, "pictures/castle/0.png");
        rightCastle = new RightCastle(580, castleY, 300, 300, 10000, "pictures/castle/0.png");


        backgroundTexture = new Texture("pictures/bg/Game.jpg");


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


        batch.draw(backgroundTexture, 0, 10, screenWidth, screenHeight);


        leftCastle.draw(batch);
        rightCastle.draw(batch);


        updateAndDrawUnits(alliedUnits, rightCastle);
        updateAndDrawUnits(enemyUnits, leftCastle);

        batch.end();
    }

    private void resolveUnitCollision(Unit unit1, Unit unit2) {
        float overlap = 50 - Math.abs(unit1.getX() - unit2.getX());
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


            for (Unit otherUnit : units) {
                if (unit != otherUnit && unit.isCollidingWith(otherUnit)) {
                    resolveUnitCollision(unit, otherUnit);
                }
            }


            if (targetCastle instanceof LeftCastle && ((LeftCastle) targetCastle).isCollidingWith(unit)) {
                unit.setIdle(true);
                ((LeftCastle) targetCastle).takeDamage(unit.getAttackDamage());
                if (((LeftCastle) targetCastle).isDestroyed()) {
                    endGame("Enemies");
                }
                unit.updateAndDraw(batch, elapsedTime, new ArrayList<>());
                continue;
            }

            if (targetCastle instanceof RightCastle && ((RightCastle) targetCastle).isCollidingWith(unit)) {
                unit.setIdle(true); // Stop the unit
                ((RightCastle) targetCastle).takeDamage(unit.getAttackDamage());
                if (((RightCastle) targetCastle).isDestroyed()) {
                    endGame("Allies");
                }
                unit.updateAndDraw(batch, elapsedTime, new ArrayList<>());
                continue;
            }

            if (!unit.isAlive()) {
                iterator.remove();
                continue;
            }

            boolean isFighting = false;


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



    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case com.badlogic.gdx.Input.Keys.NUM_1:
                addAlliedUnit(new Knight(0, commonY));
                break;
            case com.badlogic.gdx.Input.Keys.NUM_2:
                addAlliedUnit(new Wizard(0, commonY));
                break;
            case com.badlogic.gdx.Input.Keys.NUM_3:
                addAlliedUnit(new Gorgon(0, commonY));
                break;
            case com.badlogic.gdx.Input.Keys.NUM_4:
                addAlliedUnit(new Drake(0, commonY));
                break;


            case com.badlogic.gdx.Input.Keys.U:
                addEnemyUnit(new Samurai(660, commonY));
                break;
            case com.badlogic.gdx.Input.Keys.I:
                addEnemyUnit(new Skeleton(660, commonY));
                break;

            case com.badlogic.gdx.Input.Keys.O:
                addEnemyUnit(new Yokai(660, commonY));
                break;

            case com.badlogic.gdx.Input.Keys.P:
                addEnemyUnit(new WereWolf(660, commonY));
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
        return false;
    }
}
