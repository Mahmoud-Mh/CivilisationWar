package io.github.civilisation;

import com.badlogic.gdx.ApplicationAdapter;

public class Main extends ApplicationAdapter {
    private GameWorld gameWorld;

    @Override
    public void create() {
        gameWorld = new GameWorld();
    }

    @Override
    public void render() {
        gameWorld.updateAndRender();
    }

    @Override
    public void dispose() {
        gameWorld.dispose();
    }
}
