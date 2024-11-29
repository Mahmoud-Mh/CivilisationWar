package io.github.civilisation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.civilisation.Units.Unit;
import io.github.civilisation.Units.Knight;
import io.github.civilisation.Units.Samurai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    private SpriteBatch batch;
    private List<Unit> alliedUnits;
    private List<Unit> enemyUnits;
    private float elapsedTime;
    private Texture backgroundTexture;
    private Texture leftsideTexture;
    private Texture rightsideTexture;
    private float commonY;
    private Turret alliedTurret;
    private Turret enemyTurret;

    public GameWorld() {
        batch = new SpriteBatch();
        alliedUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        elapsedTime = 0f;

        addAlliedUnit(new Knight(140, 100));
        addEnemyUnit(new Samurai(600, 100));

        alliedTurret = new Turret("Allied Base", 100, 300);
        enemyTurret = new Turret("Enemy Base", 100, 300);

        backgroundTexture = new Texture("pictures/bg/Game.jpg");
        leftsideTexture = new Texture("pictures/castle/0.png");
        rightsideTexture = new Texture("pictures/castle/0.png");
        commonY = 100;
    }

    public void addAlliedUnit(Unit unit) {
        alliedUnits.add(unit);
    }

    public void addEnemyUnit(Unit unit) {
        enemyUnits.add(unit);
    }

    public void updateAndRender() {
        System.out.println("updateAndRender called");

        elapsedTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0);

        float castleWidth = 300;
        float castleHeight = 300;
        batch.draw(leftsideTexture, 20, commonY, castleWidth, castleHeight);

        float screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        batch.draw(rightsideTexture, screenWidth - castleWidth - 20, commonY, castleWidth, castleHeight);

        updateAndDrawUnits(alliedUnits, enemyUnits);
        updateAndDrawUnits(enemyUnits, alliedUnits);

        batch.end();
    }

    private void updateAndDrawUnits(List<Unit> units, List<Unit> enemyUnits) {
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();


            if (!unit.isAlive()) {
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
                    if (!unit.isAlive()) {
                        iterator.remove();
                        break;
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
}
