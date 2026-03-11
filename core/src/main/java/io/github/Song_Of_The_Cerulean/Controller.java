package io.github.Song_Of_The_Cerulean;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controller {
    private Stage stage;
    private Viewport viewport;
    private Touchpad touchpad;
    private Texture bgTexture, knobTexture;

    public Controller() {
        // On utilise un FitViewport pour que l'interface garde la même taille sur tous les écrans
        viewport = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(viewport);

        // --- GÉNÉRATION DYNAMIQUE DES TEXTURES RONDES ---

        // 1. Créer le Fond (Grand cercle semi-transparent)
        Pixmap pixmapBg = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
        pixmapBg.setColor(1, 1, 1, 0.2f); // Blanc à 20% d'opacité
        pixmapBg.fillCircle(100, 100, 95); // Dessine un cercle plein
        pixmapBg.setColor(1, 1, 1, 0.5f); // Bordure un peu plus visible
        pixmapBg.drawCircle(100, 100, 95);
        bgTexture = new Texture(pixmapBg);
        pixmapBg.dispose(); // Libère la mémoire du Pixmap

        // 2. Créer le Bouton (Petit cercle plein plus opaque)
        Pixmap pixmapKnob = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmapKnob.setColor(1, 1, 1, 0.6f); // Blanc à 60% d'opacité
        pixmapKnob.fillCircle(50, 50, 45);
        knobTexture = new Texture(pixmapKnob);
        pixmapKnob.dispose();

        // 3. Appliquer les textures au style du Touchpad
        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        style.background = new TextureRegionDrawable(new TextureRegion(bgTexture));
        style.knob = new TextureRegionDrawable(new TextureRegion(knobTexture));

        // 4. Créer le Touchpad (10 = zone morte au centre)
        touchpad = new Touchpad(10, style);

        // On définit la position et la taille (130x130 pixels ici)
        touchpad.setBounds(30, 30, 130, 130);

        stage.addActor(touchpad);
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        // Le paramètre "true" centre le viewport sur l'écran
        viewport.update(width, height, true);
    }

    // Retourne une valeur entre -1 (gauche) et 1 (droite)
    public float getXPercent() {
        return touchpad.getKnobPercentX();
    }

    // Retourne une valeur entre -1 (bas) et 1 (haut)
    public float getYPercent() {
        return touchpad.getKnobPercentY();
    }

    public Stage getStage() {
        return stage;
    }

    // Très important : libérer la mémoire quand on quitte le jeu
    public void dispose() {
        bgTexture.dispose();
        knobTexture.dispose();
        stage.dispose();
    }
}
