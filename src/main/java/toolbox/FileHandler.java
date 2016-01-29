package toolbox;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by dormunis on 29/1/2016.
 */
public class FileHandler {

    private static final String DEV_PATH = "src" + File.separator + "main";
    private static final String NATIVES = "natives";
    private static final String RESOURCES_PATH = DEV_PATH + File.separator + "resources";
    private static final String SHADERS = DEV_PATH + File.separator + "shaders";
    private static final String BLEND_MAPS = RESOURCES_PATH + File.separator + "blendmaps";
    private static final String DUDV = RESOURCES_PATH + File.separator + "dudv";
    private static final String FONTS = RESOURCES_PATH + File.separator + "fonts";
    private static final String GUI_ELEMENTS = RESOURCES_PATH + File.separator + "gui";
    private static final String TEXTURE_MAPS = RESOURCES_PATH + File.separator + "maps";
    private static final String NORMAL_MAPS = RESOURCES_PATH + File.separator + "normalmaps";
    private static final String OBJECTS = RESOURCES_PATH + File.separator + "objects";
    private static final String SKYBOX = RESOURCES_PATH + File.separator + "skybox";
    private static final String[] SKYBOX_LIST = new String[] {"right", "left", "top", "bottom", "back", "front"};

    private static final String OBJECT_EXT = ".obj";
    private static final String GLSL_EXT = ".glsl";
    private static final String MAP_EXT = ".png";
    private static final String FONT_EXT = ".fnt";

    public String getTexture(String fileName) {
        return TEXTURE_MAPS + File.separator + fileName + MAP_EXT;
    }

    public String getBlendMap(String blendMap) {
        return BLEND_MAPS + File.separator + blendMap + MAP_EXT;
    }

    public String getDUDV(String dudv) {
        return DUDV + File.separator + dudv + MAP_EXT;
    }

    public String getGuiElement(String gui) {
        return GUI_ELEMENTS + File.separator + gui + MAP_EXT;
    }

    public String getNormalMaps(String name) {
        return NORMAL_MAPS + File.separator + name + MAP_EXT;
    }

    public String[] getSkyBox(String name) {
        String[] skybox = new String[SKYBOX_LIST.length];
        for (int i = 0; i < skybox.length; i++)
            skybox[i] = SKYBOX + File.separator + name + File.separator + SKYBOX_LIST[i] + MAP_EXT;
        return skybox;
    }

    public String getShader(String shader) {
        return SHADERS + File.separator + shader + GLSL_EXT;
    }

    public String getFont(String font) {
        return FONTS + File.separator + font + FONT_EXT;
    }

    public String getModel(String model) {
        return OBJECTS + File.separator + model + OBJECT_EXT;
    }

    public void loadNatives() throws URISyntaxException {
        String os = System.getProperty("os.name");
        System.setProperty("org.lwjgl.librarypath", new File(NATIVES + File.separator + os.toLowerCase()).getAbsolutePath());
    }
}

