package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gorgon extends Unit {
    public Gorgon(float x, float y) {
        super(x, y, 300, 80, 20, UnitType.TANK);
        this.walkAnimation = AnimationFactory.create("assets/pictures/Gorgon/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/Gorgon/Attack.png", 73, 3, 0.2f);
    }

    @Override
    public void move() {

        this.x -= speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime(); // Déplacement vers la gauche
    }
}
