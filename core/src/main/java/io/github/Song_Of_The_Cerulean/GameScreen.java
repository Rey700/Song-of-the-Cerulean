package io.github.Song_Of_The_Cerulean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private boolean debug = false;
    private Main game;

    // AJOUT DU CONTROLLER
    private Controller controller;

    public GameScreen(Main game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        map = new TmxMapLoader().load("maps/Test_Stage.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        createCollisions();
        Vector2 spawn = getSpawnPoint();
        player = new Player(world, spawn.x, spawn.y);

        // INITIALISATION DU CONTROLLER
        controller = new Controller();
        // Indispensable pour que le joystick réagisse au toucher
        Gdx.input.setInputProcessor(controller.getStage());
    }

    private void update(float delta) {
        world.step(1 / 60f, 6, 2);

        // PASSAGE DES INPUTS AU JOUEUR
        // Tu peux ajuster le "200" pour changer la vitesse de course
        player.handleInput(controller.getXPercent(), 200f);

        player.update(delta);

        camera.position.x = player.getBody().getPosition().x;
        camera.position.y = player.getBody().getPosition().y;
        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0,0,0,1);

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        // DESSIN DU JOYSTICK APRES LE MONDE (AU PREMIER PLAN)
        controller.draw();

        if(debug){
            debugRenderer.render(world, camera.combined);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 800;
        camera.viewportHeight = 480 * ((float)height / width);
        camera.update();
        // REDIMENSIONNER LE HUD
        controller.resize(width, height);
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        batch.dispose();
        world.dispose();
        debugRenderer.dispose();
        player.dispose();
    }

    @Override public void show(){}
    @Override public void hide(){}
    @Override public void pause(){}
    @Override public void resume(){}

    // Fonctions getSpawnPoint() et createCollisions() identiques à ton code original...
    private Vector2 getSpawnPoint() {
        float x = 100; float y = 200;
        MapObject spawn = map.getLayers().get("Objets").getObjects().get("Lym_Spawn");
        if (spawn instanceof PointMapObject) {
            PointMapObject point = (PointMapObject) spawn;
            x = point.getPoint().x; y = point.getPoint().y;
        }
        return new Vector2(x, y);
    }

    private void createCollisions() {
        for (MapObject object : map.getLayers().get("Collisions").getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rect.x + rect.width / 2, rect.y + rect.height / 2);
                Body body = world.createBody(bodyDef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.width / 2, rect.height / 2);
                body.createFixture(shape, 0);
                shape.dispose();
            }
        }
    }
}
