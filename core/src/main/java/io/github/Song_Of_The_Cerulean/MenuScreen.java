package io.github.Song_Of_The_Cerulean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {

    private Stage stage;
    private Texture background;
    private Main game;
    private Music titleMusic;
    private Texture buttonTexture;
    private float drawWidth, drawHeight, backgroundX, backgroundY;
    private Viewport viewport;
    private Texture backgroundFill;   // fond étendu
    private Texture backgroundMain;   // image principale
    public MenuScreen(Main game) {
        this.game = game;

        // Stage avec viewport
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        // Musique
        titleMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/The-Cerulean-returns.mp3"));
        titleMusic.setLooping(true); // boucle
        titleMusic.setVolume(1f);  // volume (0 à 1)
        titleMusic.play();

        // Background
        backgroundFill = new Texture("images/background/Background_Blurr.png");
        backgroundMain = new Texture("images/background/Title_Background.png");

        // Skin UI
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Charger la texture du bouton
        buttonTexture = new Texture("images/Button/NewGame.png");
        Drawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

//      Créer le style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonDrawable;    // état normal
        style.down = buttonDrawable;  // état pressé (ici on garde la même image)
        style.font = skin.getFont("default-font");
        style.fontColor = Color.WHITE;

        // Créer le bouton avec ce style
        TextButton newGameBtn = new TextButton("", style);

        newGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        newGameBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newGameBtn.setColor(Color.LIGHT_GRAY); // bouton devient gris
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                newGameBtn.setColor(Color.WHITE); // bouton revient à normal
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(newGameBtn);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        stage.getBatch().begin();

        // ===== FOND REMPLI (peut être coupé) =====
        float fillScale = Math.max(
            worldWidth / backgroundFill.getWidth(),
            worldHeight / backgroundFill.getHeight()
        );

        float fillW = backgroundFill.getWidth() * fillScale;
        float fillH = backgroundFill.getHeight() * fillScale;

        stage.getBatch().draw(
            backgroundFill,
            (worldWidth - fillW) / 2f,
            (worldHeight - fillH) / 2f,
            fillW,
            fillH
        );

        // ===== IMAGE PRINCIPALE (jamais coupée) =====
        float mainScale = Math.min(
            worldWidth / backgroundMain.getWidth(),
            worldHeight / backgroundMain.getHeight()
        );

        float mainW = backgroundMain.getWidth() * mainScale;
        float mainH = backgroundMain.getHeight() * mainScale;

        float mainX = (worldWidth - mainW) / 2f;
        float mainY = (worldHeight - mainH) / 2f;

        stage.getBatch().draw(backgroundMain, mainX, mainY, mainW, mainH);

        stage.getBatch().end();

        updateButtonPosition(mainW, mainH, mainX, mainY);

        stage.act(delta);
        stage.draw();
    }

    private void updateButtonPosition(float w, float h, float x, float y) {
        TextButton btn = (TextButton) stage.getActors().get(0);

        btn.pack();

        float centerX = x + w * 0.5f;
        float centerY = y + h * 0.35f;

        btn.setPosition(centerX - btn.getWidth()/2f,
            centerY - btn.getHeight()/2f);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        buttonTexture.dispose();
        titleMusic.dispose();
        background.dispose();
    }

    @Override
    public void hide() {
        titleMusic.stop();
    }
}
