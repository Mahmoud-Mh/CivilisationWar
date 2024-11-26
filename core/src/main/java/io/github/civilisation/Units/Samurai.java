package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Samurai extends Unit {

    public Samurai(float x, float y) {
        super(x, y, 100, 30, 50, UnitType.MELEE);

        // Initialisation des animations
        this.walkAnimation = AnimationFactory.create("pictures/samurai/Samurai/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("pictures/samurai/Samurai/Attack_1.png", 73, 3, 0.2f);

        // Orienter le samurai vers la gauche
        this.setFacingRight(false);
    }

    @Override
    public void move() {
        // Déplacement vers la gauche
        this.x -= speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();
    }
    public boolean isCollidingWith(Unit other) {
        // Vérifie si la distance horizontale entre les unités est inférieure à une certaine valeur
        return Math.abs(this.x - other.x) < 50; // 50 est une marge ajustable
    }

    public void fight(Unit other) {
        if (this.isAlive() && other.isAlive()) {
            other.takeDamage(this.attackDamage); // Inflige des dégâts à l'autre unité
            this.takeDamage(other.attackDamage); // Reçoit des dégâts de l'autre unité
        }
    }


}
