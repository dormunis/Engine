//package engineTester;
//
//import com.bulletphysics.collision.broadphase.BroadphaseInterface;
//import com.bulletphysics.collision.broadphase.DbvtBroadphase;
//import com.bulletphysics.collision.dispatch.CollisionConfiguration;
//import com.bulletphysics.collision.dispatch.CollisionDispatcher;
//import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
//import com.bulletphysics.collision.shapes.BoxShape;
//import com.bulletphysics.collision.shapes.CollisionShape;
//import com.bulletphysics.collision.shapes.SphereShape;
//import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
//import com.bulletphysics.dynamics.DynamicsWorld;
//import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
//import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
//import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
//import com.bulletphysics.linearmath.DefaultMotionState;
//import com.bulletphysics.linearmath.MotionState;
//import com.bulletphysics.linearmath.Transform;
//import entities.Camera;
//import entities.Entity;
//import entities.Light;
//import entities.Player;
//import models.TexturedModel;
//import normalMappingObjConverter.NormalMappedObjLoader;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.util.vector.Vector3f;
//import org.lwjgl.util.vector.Vector4f;
//import particles.ParticleMaster;
//import particles.ParticleSystem;
//import particles.ParticleTexture;
//import renderEngine.DisplayManager;
//import renderEngine.Loader;
//import renderEngine.MasterRenderer;
//import renderEngine.OBJLoader;
//import terrains.Terrain;
//import textures.ModelTexture;
//
//import javax.vecmath.Matrix4f;
//import javax.vecmath.Quat4f;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class MazeGameLoop {
//
//
//    public static void run() throws URISyntaxException {
//        DisplayManager.createDisplay();
//        Loader loader = new Loader();
//        MasterRenderer renderer = new MasterRenderer(loader);
//        ParticleMaster.init(loader, renderer.getProjectionMatrix());
//
//        BroadphaseInterface broadphase = new DbvtBroadphase();
//        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
//        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
//        ConstraintSolver solver = new SequentialImpulseConstraintSolver();
//
//        DynamicsWorld dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
//        dynamicsWorld.setGravity(new javax.vecmath.Vector3f(0,0,0));
//        dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0f;
//
//        List<Entity> entities = new ArrayList<>();
//        List<Entity> normalMapEntities = new ArrayList<>();
//
//        // box model creation
//        TexturedModel boxModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("cube", loader), new ModelTexture(loader.loadTexture("wall4")));
//        boxModel.getTexture().setNormalMap(loader.loadTexture("wall4NormalMap"));
//        float boxScale = 8;
//        boxModel.getTexture().setShineDamper(1);
//        boxModel.getTexture().setReflectivity(2);
//        CollisionShape boxShape = new BoxShape(new javax.vecmath.Vector3f(1,1,1));
//        MotionState boxMotionState = new DefaultMotionState(
//                new Transform(
//                        new Matrix4f(
//                                new Quat4f(0,0,0,1),
//                                new javax.vecmath.Vector3f(0, 1, 0),
//                                1.0f
//                        )
//                )
//        );
//        boxShape.setLocalScaling(new javax.vecmath.Vector3f(boxScale, boxScale, boxScale));
//        float boxMass = 0;
//        javax.vecmath.Vector3f boxInertia = new javax.vecmath.Vector3f(0,0,0);
//        boxShape.calculateLocalInertia(boxMass, boxInertia);
//        RigidBodyConstructionInfo boxConstruction = new RigidBodyConstructionInfo(boxMass, boxMotionState, boxShape, boxInertia);
//        boxConstruction.restitution = 0.25f;
//
//        //player model creation
//        TexturedModel playerModel = new TexturedModel(OBJLoader.loadObjModel("sphere", loader), new ModelTexture(loader.loadTexture("cloud")));
//        playerModel.getTexture().setShineDamper(0);
//        playerModel.getTexture().setReflectivity(0);
//        float ballRadius = 3.0f;
//        CollisionShape sphereShape = new SphereShape(ballRadius);
//        MotionState playerMotionState = new DefaultMotionState(
//                new Transform(
//                        new Matrix4f(
//                                new Quat4f(0,0,0,1),
//                                new javax.vecmath.Vector3f(0, ballRadius, 0),
//                                1.0f
//                        )
//                )
//        );
//        javax.vecmath.Vector3f ballInertia = new javax.vecmath.Vector3f(0,0,0);
//        float playerMass = 2.5f;
//        sphereShape.calculateLocalInertia(playerMass, ballInertia);
//        RigidBodyConstructionInfo playerConstruction = new RigidBodyConstructionInfo(playerMass, playerMotionState, sphereShape, ballInertia);
//        playerConstruction.restitution = 0.5f;
//        playerConstruction.angularDamping = 0.95f;
//
//        Vector3f playerLocation = new Vector3f(0,0,0);
//        Player player = new Player(playerModel, playerLocation, new Vector3f(0,0,0), 1);
//
//        // add light to player
//        Light light = new Light(playerLocation, new Vector3f(0.8f,0.8f,1), new Vector3f(0.5f, 0.2f, 0.01f));
//        light.enableFlicker(true, new Vector3f(-0.49f, -0.15f, 0.01f));
//        List<Light> lights = Collections.singletonList(light);
//
//
//        // handle entities
//        entities.add(player);
//        for (int i=-150;i<150;i+=boxScale) {
//            entities.add(new Entity(boxModel, new Vector3f(boxScale-boxScale/2,-boxScale/2,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(boxScale-boxScale/2,boxScale-boxScale/2,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(boxScale-boxScale/2,-boxScale/2-boxScale,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(-boxScale-boxScale/2,-boxScale/2,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(-boxScale-boxScale/2,boxScale-boxScale/2,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(-boxScale-boxScale/2,-boxScale/2-boxScale,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(-boxScale/2,-boxScale/2+boxScale,i), new Vector3f(0,0,0), boxScale));
//            entities.add(new Entity(boxModel, new Vector3f(-boxScale/2,-boxScale/2-boxScale,i), new Vector3f(0,0,0), boxScale));
//        }
//        Camera camera = new Camera(player);
//
//
//        ParticleTexture cosmicTexture = new ParticleTexture(loader.loadTexture("cosmic"), 4);
//        cosmicTexture.setAdditive(true);
//        ParticleSystem cosmicSystem = new ParticleSystem(cosmicTexture, 60, 1, 1f, 1f, 2.3f);
//        cosmicSystem.setDirection(new Vector3f(0, 2, 0), 0.1f);
//        cosmicSystem.setLifeError(0.2f);
//        cosmicSystem.setSpeedError(0.2f);
//        cosmicSystem.setScaleError(0.6f);
//        cosmicSystem.randomizeRotation();
//
//        while (!Display.isCloseRequested()) {
//            player.move(null);
//            camera.move();
//            cosmicSystem.generateParticles(new Vector3f(player.getPosition()));
//            dynamicsWorld.stepSimulation(DisplayManager.getFrameTimeSeconds());
//            ParticleMaster.update(camera);
//
//            renderer.renderScene(entities, normalMapEntities, Collections.<Terrain>emptyList(), lights, camera, new Vector4f(0, -1, 0, 100000));
//            ParticleMaster.render(camera);
//            DisplayManager.updateDisplay();
//        }
//
//        ParticleMaster.cleanUp();
//        renderer.cleanUp();
//        loader.cleanUp();
//        DisplayManager.closeDisplay();
//
//    }
//
//
//}
