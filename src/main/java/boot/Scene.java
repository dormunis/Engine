package boot;

import com.google.common.collect.Lists;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import render.utils.NormalMappedObjLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particles.ParticleMaster;
import particles.ParticleSystem;
import textures.ParticleTexture;
import render.DisplayManager;
import render.utils.Loader;
import render.MasterRenderer;
import render.utils.ObjectLoader;
import terrains.Terrain;
import textures.ModelTexture;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Scene {

    private static final float BOX_SCALE = 1;

    private static Set<Entity> entities = new HashSet<>();
    private static Set<Entity> normalMapEntities = new HashSet<>();
    private static Set<Light> lights = new HashSet<>();
    private static Set<Terrain> terrains = new HashSet<>();
    private static Camera camera;
    private static Player player;
    private static ParticleSystem cosmicSystem;

    private static Loader loader;
    private static MasterRenderer renderer;

    public static void run() throws URISyntaxException {
        DisplayManager.createDisplay();
        loader = new Loader();
        renderer = new MasterRenderer(loader);
        setup();
        initiateGameLoop();
        cleanUp();
    }

    private static void setup() {
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        terrains = Collections.emptySet();
        setupPhysics();

        // handle entities
        createBox(new Vector3f(0,0,20));
        createPlayer(new Vector3f(0,0,0));
        createCosmicTexture();
        camera = new Camera(player);
    }

    private static void setupPhysics() {
    }


    private static void createBox(Vector3f boxPosition) {
        // box model creation
        TexturedModel boxModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("cube", loader), new ModelTexture(loader.loadTexture("wall4")));
        boxModel.getTexture().setNormalMap(loader.loadTextureNormalMap("wall4"));
        boxModel.getTexture().setShineDamper(5);
        boxModel.getTexture().setReflectivity(1);
        Entity box = new Entity(boxModel, boxPosition, new Vector3f(0,0,0), BOX_SCALE);
        box.setScale(4);

        entities.add(box);
    }

    private static void createCosmicTexture() {
        ParticleTexture cosmicTexture = new ParticleTexture(loader.loadTexture("cosmic"), 4);
        cosmicTexture.setAdditive(true);
        cosmicSystem = new ParticleSystem(cosmicTexture, 60, 1, 1f, 1f, 2.3f);
        cosmicSystem.setDirection(new Vector3f(0, 2, 0), 0.1f);
        cosmicSystem.setLifeError(0.2f);
        cosmicSystem.setSpeedError(0.2f);
        cosmicSystem.setScaleError(0.6f);
        cosmicSystem.randomizeRotation();
    }

    private static void createPlayer(Vector3f playerLocation) {
        //player model creation
        TexturedModel playerModel = new TexturedModel(ObjectLoader.loadObjModel("sphere", loader), new ModelTexture(loader.loadTexture("cloud")));
        playerModel.getTexture().setShineDamper(0.01f);
        playerModel.getTexture().setReflectivity(0.1f);
        player = new Player(playerModel, playerLocation, new Vector3f(0,0,0), 1);

        // add light to player
        Light light = new Light(playerLocation, new Vector3f(0.8f,0.8f,1), new Vector3f(0.5f, 0.2f, 0.01f));
        light.enableFlicker(true, new Vector3f(-0.49f, -0.15f, 0.01f));
        lights = Collections.singleton(light);

        entities.add(player);
    }

    private static void initiateGameLoop() {
        while (!Display.isCloseRequested()) {
            render();
            logic();
            input();
            update();
        }
    }

    private static void cleanUp() {
        ParticleMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static void update() {
        DisplayManager.updateDisplay();
    }

    private static void render() {
        renderer.renderScene(
                Lists.newArrayList(entities),
                Lists.newArrayList(normalMapEntities),
                Lists.newArrayList(terrains),
                Lists.newArrayList(lights),
                camera,
                new Vector4f(0, -1, 0, 100000)
        );
        ParticleMaster.render(camera);
        DisplayManager.updateDisplay();
    }

    private static void logic() {
        player.move(null, entities.stream().filter(e->!e.equals(player)).collect(Collectors.toList()));
        camera.move();
        cosmicSystem.generateParticles(new Vector3f(player.getPosition()));
        ParticleMaster.update(camera);
    }

    private static void input() {
        mouseControls();
        keyboardControls();
    }

    private static void mouseControls() {
        int dx = 0;
        int dy = 0;
        int dz = 0;
        int zoom = Mouse.getDWheel();

        if (Mouse.isButtonDown(1)) {
            dx = Mouse.getX();
            dy = Mouse.getY();
        }

        if (Mouse.isButtonDown(0)) {
            dx = Mouse.getX();
            dy = Mouse.getY();
            camera.setOffsetCamera(true);
        } else
            camera.setOffsetCamera(false);

        camera.storeInput(dx, dy, dz, zoom);
    }

    private static void keyboardControls() {
        // forward movement
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            player.currentSpeed = Player.RUN_SPEED;

        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            player.currentSpeed = -Player.RUN_SPEED;

        else
            player.currentSpeed = 0;

        //strafing movement
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            player.currentTurnSpeed = -Player.TURN_SPEED;

        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
            player.currentTurnSpeed = Player.TURN_SPEED;

        else
            player.currentTurnSpeed = 0;
    }

}
