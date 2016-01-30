package engine.toolbox;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;
import java.util.List;

public class Utils {

    public static Vector listToVector(List<String> list) {
        if (list.size() == 3)
            return new Vector3f(Float.valueOf(list.get(0)), Float.valueOf(list.get(1)), Float.valueOf(list.get(2)));
        else if (list.size() == 2)
            return new Vector2f(Float.valueOf(list.get(0)), Float.valueOf(list.get(1)));
        return null;
    }

    public static Vector listToVector(String... list) {
        return listToVector(Arrays.asList(list));
    }
}
