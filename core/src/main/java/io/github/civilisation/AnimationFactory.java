package io.github.civilisation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationFactory {
    public static Animation<TextureRegion> create(String path, int frameWidth, int frameCount, float frameDuration, boolean flipX) {
        Texture texture = new Texture(path);
        TextureRegion[][] frames = TextureRegion.split(texture, frameWidth, texture.getHeight());
        TextureRegion[] animationFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            animationFrames[i] = frames[0][i];
            if (flipX && !animationFrames[i].isFlipX()) {
                animationFrames[i].flip(true, false);
            }
        }
        return new Animation<>(frameDuration, animationFrames);
    }

    public static Animation<TextureRegion> create(String path, int frameWidth, int frameCount, float frameDuration) {
        return create(path, frameWidth, frameCount, frameDuration, false);
    }
}
