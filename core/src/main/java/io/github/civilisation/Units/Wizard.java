package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wizard extends Unit {

    public Wizard(float x, float y) {
        super(x, y, 70, 50, 30, UnitType.RANGED); // 70 PV, 50 dégâts, 30 vitesse
        this.walkAnimation = AnimationFactory.create("assets/pictures/Wizard/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/Wizard/Attack.png", 73, 3, 0.2f);
    }

    @Override
    public void move() {
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Déplacement vers la droite
    }
}
