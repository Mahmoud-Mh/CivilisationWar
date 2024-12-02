package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WereWolf extends Unit {

    public WereWolf(float x, float y) {
        super(x, y, 300, 100, 40, UnitType.TANK);
        this.walkAnimation = AnimationFactory.create("assets/pictures/Knight/Knight_1/Walk.png", 73, 5, 0.1f);

        this.attackAnimation = AnimationFactory.create("assets/pictures/WereWolf/Attack.png", 73, 3, 0.2f);
    }

    @Override
    public void move() {
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Déplacement vers la droite
    }
}
