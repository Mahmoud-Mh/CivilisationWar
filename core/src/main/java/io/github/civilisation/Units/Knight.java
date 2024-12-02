package io.github.civilisation.Units;

import io.github.civilisation.AnimationFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Knight extends Unit {
    public Knight(float x, float y) {
        super(x, y, 800, 50, 50, UnitType.MELEE); // 800 PV, 50 dégâts, 50 vitesse

        this.walkAnimation = AnimationFactory.create("pictures/Knight/Knight_1/Walk.png", 73, 5, 0.1f);

        this.attackAnimation = AnimationFactory.create("pictures/Knight/Knight_1/Attack 1.png", 73, 3, 0.2f);
    }

    @Override
    public void move() {
        float previousX = this.x;
        this.x += speed * com.badlogic.gdx.Gdx.graphics.getDeltaTime();


        if (this.x > com.badlogic.gdx.Gdx.graphics.getWidth() - 50) {
            this.x = com.badlogic.gdx.Gdx.graphics.getWidth() - 50;
        }


        if (this.x == previousX) {
            this.isFighting = false;
        }
    }

}

