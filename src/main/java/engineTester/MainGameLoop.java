//package engineTester;
//
//import entities.Camera;
//import entities.Entity;
//import entities.Light;
//import entities.Player;
//import fontMeshCreator.FontType;
//import fontMeshCreator.GUIText;
//import fontRendering.TextMaster;
//import guis.GuiRenderer;
//import guis.GuiTexture;
//import models.RawModel;
//import models.TexturedModel;
//import normalMappingObjConverter.NormalMappedObjLoader;
//import objConverter.OBJFileLoader;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL30;
//import org.lwjgl.util.vector.Vector2f;
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
//import textures.TerrainTexture;
//import textures.TerrainTexturePack;
//import water.WaterFrameBuffers;
//import water.WaterRenderer;
//import water.WaterShader;
//import water.WaterTile;
//import world.Biome;
//
//import java.io.File;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainGameLoop {
//
//    public static void run() throws URISyntaxException {
//        Main.fileHandler.loadNatives();
//
//        DisplayManager.createDisplay();
//        Loader loader = new Loader();
//        TextMaster.init(loader);
//        MasterRenderer renderer = new MasterRenderer(loader);
//        ParticleMaster.init(loader, renderer.getProjectionMatrix());
//
//        FontType font = new FontType(loader.loadTexture("candara"), new File(Main.fileHandler.getFont("candara")));
//        GUIText text = new GUIText("This is some text!", 3f, font, new Vector2f(0f, 0.0f), 1f, true);
//        text.setColour(0, 0, 0);
//        text.setBorderWidth(0.6f);
//        text.setOutlineColor(new Vector3f(1, 1, 1));
//
//        // *********TERRAIN TEXTURE STUFF**********
//
//        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
//        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
//        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
//        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
//
//        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
//                gTexture, bTexture);
//        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
//
//        // *****************************************
//
//        TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
//                new ModelTexture(loader.loadTexture("rocks")));
//
//        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
//        fernTextureAtlas.setNumberOfRows(2);
//
//        TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
//                fernTextureAtlas);
//
//        TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
//                new ModelTexture(loader.loadTexture("pine")));
//        bobble.getTexture().setHasTransparency(true);
//
//        fern.getTexture().setHasTransparency(true);
//
//        List<Terrain> terrains = new ArrayList<Terrain>();
//
//        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
//                new ModelTexture(loader.loadTexture("lamp")));
//        lamp.getTexture().setUseFakeLighting(true);
//
//        List<Entity> entities = new ArrayList<Entity>();
//        List<Entity> normalMapEntities = new ArrayList<Entity>();
//
//        //******************NORMAL MAP MODELS************************
//
//        TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
//                new ModelTexture(loader.loadTexture("barrel")));
//        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
//        barrelModel.getTexture().setShineDamper(10);
//        barrelModel.getTexture().setReflectivity(0.5f);
//
////        normalMapEntities.add(new Entity(barrelModel, new Vector3f(75,10,-75),0,0,0,1f));
//
//        TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
//                new ModelTexture(loader.loadTexture("crate")));
//        crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
//        crateModel.getTexture().setShineDamper(10);
//        crateModel.getTexture().setReflectivity(0.5f);
//
//        TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
//                new ModelTexture(loader.loadTexture("boulder")));
//        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
//        boulderModel.getTexture().setShineDamper(10);
//        boulderModel.getTexture().setReflectivity(0.5f);
//
//
//        //************ENTITIES*******************
//
//        //*******************OTHER SETUP***************
//
//        List<Light> lights = new ArrayList<Light>();
//        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
//        lights.add(sun);
//
//        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
//        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
//                loader.loadTexture("playerTexture")));
//
////        Player player = new Player(stanfordBunny, new Vector3f(0, 0, 0), 0, 100, 0, 0.6f);
////        entities.add(player);
////        Camera camera = new Camera(player);
//        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
//        GuiRenderer guiRenderer = new GuiRenderer(loader);
//        // MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), null);
//
//        //**********Water Renderer Set-up************************
//
//        WaterFrameBuffers buffers = new WaterFrameBuffers();
//        WaterShader waterShader = new WaterShader();
//        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
//        List<WaterTile> waters = new ArrayList<WaterTile>();
//
//        //*********Particle System below**********************
//
//        //Fire
//        ParticleTexture fireTexture = new ParticleTexture(loader.loadTexture("fire"), 8);
//        fireTexture.setAdditive(true);
//        ParticleSystem fireSystem = new ParticleSystem(fireTexture, 400, 10, 0.1f, 2, 5f);
//        fireSystem.setDirection(new Vector3f(0, 2, 0), 0.1f);
//        fireSystem.setLifeError(0.2f);
//        fireSystem.setSpeedError(0.6f);
//        fireSystem.setScaleError(1f);
//        fireSystem.randomizeRotation();
//
//        //Cosmic
//        ParticleTexture cosmicTexture = new ParticleTexture(loader.loadTexture("cosmic"), 4);
//        cosmicTexture.setAdditive(true);
//        ParticleSystem cosmicSystem = new ParticleSystem(cosmicTexture, 200, 10, 0.1f, 1, 2f);
//        fireSystem.setDirection(new Vector3f(0, 2, 0), 0.1f);
//        fireSystem.setLifeError(0.2f);
//        fireSystem.setSpeedError(0.6f);
//        fireSystem.setScaleError(1f);
//        fireSystem.randomizeRotation();
//
//
//        //****************Biome Code below*********************
//        Biome.init(loader, texturePack, blendMap, terrains, waters, entities);
//        List<TexturedModel> possibleModels = new ArrayList<TexturedModel>();
//        possibleModels.add(fern);
//        possibleModels.add(bobble);
//        Biome main = new Biome(0, -1, -10, possibleModels);
//
//        //****************Game Loop Below*********************
//
//        while (!Display.isCloseRequested()) {
//            player.move(main.getTerrain());
//            camera.move();
//            //picker.update();
//
//            if (Keyboard.isKeyDown(Keyboard.KEY_U))
//                fireSystem.generateParticles(new Vector3f(player.getPosition()));
//            if (Keyboard.isKeyDown(Keyboard.KEY_I))
//                cosmicSystem.generateParticles(new Vector3f(player.getPosition()));
//
//            ParticleMaster.update(camera);
//
//            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
//
//            for (WaterTile water : waters) {
//                //render reflection texture
//                buffers.bindReflectionFrameBuffer();
//                float distance = 2 * (camera.getPosition().y - water.getHeight());
//                camera.getPosition().y -= distance;
//                camera.invertPitch();
//                renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
//                camera.getPosition().y += distance;
//                camera.invertPitch();
//
//                //render refraction texture
//                buffers.bindRefractionFrameBuffer();
//                renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
//            }
//
//            //render to screen
//            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
//            buffers.unbindCurrentFrameBuffer();
//            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
//            waterRenderer.render(waters, camera, sun);
//            ParticleMaster.render(camera);
//            guiRenderer.render(guiTextures);
//            TextMaster.render();
//
//            DisplayManager.updateDisplay();
//        }
//
//        //*********Clean Up Below**************
//        ParticleMaster.cleanUp();
//        TextMaster.cleanUp();
//        buffers.cleanUp();
//        waterShader.cleanUp();
//        guiRenderer.cleanUp();
//        renderer.cleanUp();
//        loader.cleanUp();
//        DisplayManager.closeDisplay();
//
//    }
//
//
//}
