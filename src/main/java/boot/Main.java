package boot;

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
        int[][][] maze = new int[][][] {
                // LEVEL 1
                {
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 0, 1, 1, 1},
                        {1, 0, 1, 1, 1}
                },

                // LEVEL 2
                {
                        {1, 1, 1, 1, 1},
                        {1, 0, 0, 1, 1},
                        {1, 1, 0, 1, 1},
                        {1, 0, 0, 1, 1},
                        {1, 1, 1, 1, 1}
                },

                // LEVEL 3
                {
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 0, 1},
                        {1, 1, 0, 0, 1},
                        {1, 1, 1, 0, 1},
                        {1, 1, 1, 1, 1}
                },

                // LEVEL 4
                {
                        {1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 1},
                        {1, 1, 0, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1}
                },

                // LEVEL 5
                {
                        {1, 1, 0, 1, 1},
                        {1, 1, 0, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1}
                }

        };
        Scene.start(maze);
    }
}
