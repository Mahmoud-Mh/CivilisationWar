package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Drake extends Unit {
    public Drake(float x, float y) {
        super(x, y, 200, 100, 30, UnitType.SPECIAL); // 200 PV, 100 dégâts, 30 vitesse
        this.walkAnimation = AnimationFactory.create("assets/pictures/Drake/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/Drake/Attack.png", 73, 3, 0.2f);
    }

    @Override
    public void move() {
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Déplacement vers la droite
    }
}
