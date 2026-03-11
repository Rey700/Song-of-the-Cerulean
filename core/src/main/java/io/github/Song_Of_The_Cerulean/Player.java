package io.github.Song_Of_The_Cerulean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Player {

    private Body body;
    private Texture idleSheet, runSheet;
    private Animation<TextureRegion> idleAnimation, runAnimation;
    private float stateTime;

    private final float WIDTH = 48;
    private final float HEIGHT = 48;

    // Ta spritesheet de course a 6 colonnes et 4 lignes (24 frames au total)
    private final int RUN_COLS = 6;
    private final int RUN_ROWS = 4;

    private boolean faceRight = true;

    public Player(World world, float x, float y){
        // --- CONFIGURATION PHYSIQUE BOX2D ---
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true; // Empêche de basculer

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH/3, HEIGHT/2); // Hitbox un peu plus fine que le sprite

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1f;
        fixture.friction = 2.0f; // Friction élevée pour s'arrêter net quand on lâche le joystick

        body.createFixture(fixture);
        shape.dispose();

        // --- CHARGEMENT DES ANIMATIONS ---

        // Animation IDLE (6x6 frames)
        idleSheet = new Texture(Gdx.files.internal("images/Lym/Lym_idle.png"));
        idleAnimation = createAnimation(idleSheet, 6, 6, 0.075f, 0, 35);

        // Animation RUN (6x4 frames)
        // D'après ton image, on commence à la frame 6 pour ignorer le démarrage
        // et on boucle jusqu'à la frame 23.
        runSheet = new Texture(Gdx.files.internal("images/Lym/Lym_running.png"));
        runAnimation = createAnimation(runSheet, RUN_COLS, RUN_ROWS, 0.10f, 9, 23);

        stateTime = 0;
    }

    /**
     * Crée une animation à partir d'une portion d'une spritesheet
     */
    private Animation<TextureRegion> createAnimation(Texture sheet, int cols, int rows, float frameDuration, int startFrame, int endFrame) {
        int frameWidth = sheet.getWidth() / cols;
        int frameHeight = sheet.getHeight() / rows;
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);

        Array<TextureRegion> allFrames = new Array<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                allFrames.add(tmp[i][j]);
            }
        }

        Array<TextureRegion> selectedFrames = new Array<>();
        for (int i = startFrame; i <= endFrame; i++) {
            if (i < allFrames.size) {
                selectedFrames.add(allFrames.get(i));
            }
        }

        return new Animation<>(frameDuration, selectedFrames);
    }

    /**
     * Applique les inputs du joystick
     */
    public void handleInput(float horizontalPercent, float speed) {
        // Seuil minimum pour éviter les micro-mouvements (zone morte)
        if (Math.abs(horizontalPercent) < 0.1f) {
            horizontalPercent = 0;
        }

        float xVelocity = horizontalPercent * speed;
        float yVelocity = body.getLinearVelocity().y;

        body.setLinearVelocity(xVelocity, yVelocity);

        // Orientation du regard
        if (xVelocity > 5f) faceRight = true;
        else if (xVelocity < -5f) faceRight = false;
    }

    public void update(float delta){
        stateTime += delta;
    }

    public void render(SpriteBatch batch){
        TextureRegion currentFrame;

        // On vérifie si le personnage court vraiment (vitesse > 10)
        if (Math.abs(body.getLinearVelocity().x) > 10f) {
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }

        Vector2 pos = body.getPosition();

        // Rendu avec inversion horizontale (flip)
        batch.draw(
            currentFrame,
            faceRight ? pos.x - WIDTH/2 : pos.x + WIDTH/2,
            pos.y - HEIGHT/2,
            faceRight ? WIDTH : -WIDTH,
            HEIGHT
        );
    }

    public Body getBody(){ return body; }

    public void dispose(){
        idleSheet.dispose();
        runSheet.dispose();
    }
}
