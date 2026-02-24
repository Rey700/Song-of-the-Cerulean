package io.github.Song_Of_The_Cerulean;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // IMPORTANT pour que les screens fonctionnent
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
