package boot;

import engine.toolbox.FileHandler;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static FileHandler fileHandler;

    public static void main(String[] args) throws IOException, URISyntaxException {
        fileHandler = new FileHandler();
        fileHandler.loadNatives();
        Scene.run();
    }
}
