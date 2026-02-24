package io.github.Song_Of_The_Cerulean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MenuScreen implements Screen {

    private Stage stage;
    private Texture background;
    private Main game;

    public MenuScreen(Main game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        background = new Texture("C:\\Users\\pret\\Desktop\\Prog mobile Niv 2\\Song of the Cerulean\\assets\\images\\background\\Title_Background.jpg");

        Skin skin = new Skin(Gdx.files.internal("C:\\Users\\pret\\Desktop\\Prog mobile Niv 2\\Song of the Cerulean\\assets\\uiskin.png"));

        TextButton newGameBtn = new TextButton("New Game", skin);
        newGameBtn.setPosition(
            Gdx.graphics.getWidth()/2f - newGameBtn.getWidth()/2f,
            Gdx.graphics.getHeight()/2f
        );

        newGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        stage.addActor(newGameBtn);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void dispose() { stage.dispose(); background.dispose(); }
    @Override public void resize(int width, int height) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
