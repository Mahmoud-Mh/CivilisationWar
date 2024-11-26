package io.github.civilisation;

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

    public GameWorld() {
        batch = new SpriteBatch();
        alliedUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        elapsedTime = 0f;

        // Deploy initial units
        addAlliedUnit(new Knight(140, 100));
        addEnemyUnit(new Samurai(600, 100));
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

        // Update and render allied units
        updateAndDrawUnits(alliedUnits, enemyUnits);

        // Update and render enemy units
        updateAndDrawUnits(enemyUnits, alliedUnits);

        batch.end();
    }

    private void updateAndDrawUnits(List<Unit> units, List<Unit> enemyUnits) {
        for (Unit unit : units) {
            // Vérifie si l'unité est vivante
            if (unit.isAlive()) {
                boolean isFighting = false;

                // Vérifie les collisions avec les unités ennemies
                for (Unit enemy : enemyUnits) {
                    if (enemy.isAlive() && unit.isCollidingWith(enemy)) {
                        // Les unités s'arrêtent et commencent à se battre
                        unit.fight(enemy);
                        isFighting = true;
                        break; // Stoppe la vérification après avoir trouvé une collision
                    }
                }

                if (!isFighting) {
                    // Si l'unité ne combat pas, elle continue de se déplacer
                    unit.move();
                }

                // Dessine l'unité
                unit.updateAndDraw(batch, elapsedTime, enemyUnits);
            }
        }
    }



    // Méthode pour libérer toutes les ressources
    public void dispose() {
        // Libérer le SpriteBatch
        if (batch != null) {
            batch.dispose();
        }

        // Libérer les ressources des unités alliées
        for (Unit unit : alliedUnits) {
            unit.dispose();
        }

        // Libérer les ressources des unités ennemies
        for (Unit unit : enemyUnits) {
            unit.dispose();
        }
    }
}
