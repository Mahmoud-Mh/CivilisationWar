package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Skeleton extends Unit {

    public Skeleton(float x, float y) {
        super(x, y, 70, 20, 40, UnitType.RANGED); // 70 PV, 20 dégâts, 40 vitesse
        this.walkAnimation = AnimationFactory.create("assets/pictures/Skeleton/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/Skeleton/Attack.png", 73, 3, 0.2f);

    }

    @Override
    public void move() {
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Déplacement vers la droite
    }
}
