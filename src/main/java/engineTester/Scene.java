package engineTester;

import com.google.common.collect.Lists;
import entities.*;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Scene {

    private static final float BOX_SCALE = 1;

    public static boolean isPlayerMovingUp = false;
    public static boolean isPlayerMovingDown = false;

    private static Set<Entity> entities = new HashSet<>();
    private static Set<Entity> normalMapEntities = new HashSet<>();
    private static Set<Light> lights = new HashSet<>();
    private static Set<Terrain> terrains = new HashSet<>();
    private static Camera camera;
    private static Player player;

    private static Loader loader;
    private static MasterRenderer renderer;

    private static boolean applyForce = false;
    private static Set<Vector3f> constraints;

    public static void run() throws URISyntaxException {
        DisplayManager.createDisplay();
        loader = new Loader();
        renderer = new MasterRenderer(loader);
        setup();
        initiateGameLoop();
        cleanUp();
    }

    private static void setup() {
        Light light = new Light(new Vector3f(10000,50000,10000), new Vector3f(0.8f,0.8f,1));
        lights = Collections.singleton(light);
        terrains = Collections.emptySet();
        setupPhysics();

        // handle entities
        createBox(new Vector3f(0,0,30));
        createPlayer(new Vector3f(0,0,0));
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
        Entity box = new Entity(boxModel, boxPosition , new Vector3f(0,0,0), BOX_SCALE);
        box.setRestitution(0.2f);

        normalMapEntities.add(box);
    }

    private static void createPlayer(Vector3f playerLocation) {
        //player model creation
        TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("sphere", loader), new ModelTexture(loader.loadTexture("cloud")));
        playerModel.getTexture().setUseFakeLighting(true);
        playerModel.getTexture().setShineDamper(5);
        playerModel.getTexture().setReflectivity(1);
        player = new Player(playerModel, playerLocation, new Vector3f(0,0,0), 1);
        player.setRestitution(0.1f);

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
    }

    private static void logic() {
        player.move(null, null);
        camera.move();
        if (applyForce) {
            Set<Entity> allEntities = entities;
            allEntities.addAll(normalMapEntities);
            List<Entity> collisions = allEntities.stream().filter(e -> !e.equals(player) && player.isColliding(e)).collect(Collectors.toList());
            if (!collisions.isEmpty())
                handleCollision(collisions);
            collisions.clear();
        }
    }

    private static void handleCollision(List<Entity> collisions) {
        constraints.clear();
        System.out.println("collision detected");
        constraints = collisions.stream().map(c->Collision.calculateCollision(player, c)).collect(Collectors.toSet());
    }

    private static void input() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            player.currentSpeed = Player.RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            player.currentSpeed = -Player.RUN_SPEED;
        } else {
            player.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            player.currentTurnSpeed = -Player.TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            player.currentTurnSpeed = Player.TURN_SPEED;
        } else {
            player.currentTurnSpeed = 0;
        }

        applyForce = player.currentSpeed > 0 || player.currentTurnSpeed > 0;
    }

}
