package io.github.civilisation;

import com.badlogic.gdx.graphics.Texture;
import io.github.civilisation.Units.Unit;
import io.github.civilisation.Units.Knight;
import io.github.civilisation.Units.Samurai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    private SpriteBatch batch;
    private List<Unit> alliedUnits;
    private List<Unit> enemyUnits;
    private float elapsedTime;
    private Texture backgroundTexture;

    public GameWorld() {
        batch = new SpriteBatch();
        alliedUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        elapsedTime = 0f;

        addAlliedUnit(new Knight(140, 100));
        addEnemyUnit(new Samurai(600, 100));

        backgroundTexture = new Texture("pictures/bg/Game.png"); // Replace with actual path to your background image

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

        batch.begin();

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
                    unit.checkCombatStatus(enemy);
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

        backgroundTexture.dispose();
    }
}
