package engineTester;

import toolbox.FileHandler;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by dormunis on 29/1/2016.
 */
public class Main {

    public static FileHandler fileHandler;

    public static void main(String[] args) throws IOException, URISyntaxException {
        fileHandler = new FileHandler();
        fileHandler.loadNatives();
        Scene.run();
    }
}
