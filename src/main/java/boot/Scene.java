package boot;

import com.google.common.collect.Lists;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particles.ParticleMaster;
import particles.ParticleSystem;
import render.DisplayManager;
import render.MasterRenderer;
import render.utils.Loader;
import render.utils.NormalMappedObjLoader;
import render.utils.ObjectLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.ParticleTexture;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Scene {

    private static final float BOX_SCALE = 10;

    private static Set<Entity> entities = new HashSet<>();
    private static Set<Entity> normalMapEntities = new HashSet<>();
    private static Set<Light> lights = new HashSet<>();
    private static Set<Terrain> terrains = new HashSet<>();
    private static Camera camera;
    private static Player player;
    private static ParticleSystem cosmicSystem;

    private static Loader loader;
    private static MasterRenderer renderer;

    private static Set<Integer> keysPressedRightNow = new HashSet<>();
    private static Set<Integer> keyBuffer = new HashSet<>();

    public static void start(int[][][] maze) throws URISyntaxException {
        DisplayManager.createDisplay();
        loader = new Loader();
        renderer = new MasterRenderer(loader);
        setup(maze);
        initiateGameLoop();
        cleanUp();
    }

    private static void setup(int[][][] maze) {
        ParticleMaster.init(loader, renderer.getProjectionMatrix());
        terrains = Collections.emptySet();
        setupPhysics();

        // handle entities
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                for (int k = 0; k < maze[i][i].length; k++) {
                    if (maze[i][j][k] != 0)
                        createBox(new Vector3f(i*BOX_SCALE*2,j*BOX_SCALE*2,k*BOX_SCALE*2));
                }
            }
        }


        createPlayer(new Vector3f(0 * BOX_SCALE - (BOX_SCALE/2),4 * BOX_SCALE - (BOX_SCALE/2),1 * BOX_SCALE - (BOX_SCALE/2)));
        createCosmicTexture();
        camera = new Camera(player);
    }

    private static void setupPhysics() {
        // TODO: TBD for collision detection
    }


    private static void createBox(Vector3f boxPosition) {
        // box model creation
        TexturedModel boxModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("cube", loader), new ModelTexture(loader.loadTexture("wall4")));
        boxModel.getTexture().setNormalMap(loader.loadTextureNormalMap("wall4"));
        boxModel.getTexture().setShineDamper(1.8f);
        boxModel.getTexture().setReflectivity(2.8f);
        Entity box = new Entity(boxModel, boxPosition, new Vector3f(0,0,0), BOX_SCALE);
        box.setScale(BOX_SCALE);

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
            input();
            logic();
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
        int zoom = Mouse.getDWheel();

        if (Mouse.isButtonDown(1)) {
            dx = Mouse.getDX();
            dy = Mouse.getDY();
        }
        if (Mouse.isButtonDown(0)) {
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            camera.unlockFromPlayer();
        } else if (!camera.isLockedToPlayer())
            camera.lockToPlayer();

        camera.storeInput(dx, dy, zoom);
    }

    private static void keyboardControls() {
        float dx, dy, dz;

        //strafing movement
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            dx = -Player.TURN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
            dx = Player.TURN_SPEED;
        else
            dx = 0;

        // up and down movement
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            dy = Player.VERTICAL_VECTOR_STRENGTH;
        else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            dy = -Player.VERTICAL_VECTOR_STRENGTH;
        else
            dy = 0;

        // forward movement
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            dz = Player.RUN_SPEED;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            dz = -Player.RUN_SPEED;
        else
            dz = 0;

        player.storeInput(dx,dy,dz);
    }
}
