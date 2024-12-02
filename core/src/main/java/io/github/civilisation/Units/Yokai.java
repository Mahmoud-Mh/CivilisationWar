package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Yokai extends Unit {

    public Yokai(float x, float y) {
        super(x, y, 250, 70, 25, UnitType.SPECIAL);
        this.walkAnimation = AnimationFactory.create("assets/pictures/Yokai/Walk.png", 73, 5, 0.1f);
        this.attackAnimation = AnimationFactory.create("assets/pictures/Yokai/Attack.png", 73, 3, 0.2f);
    }

    @Override
    public void move() {
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();
    }
}
