package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by dor on 29/01/16.
 */
public class Collision {

    public static Vector3f calculateCollision(Entity entity, Entity other) {
        Vector3f ePosition = entity.getPosition();
        float eScale = entity.getScale() / 2;
        float eRestitution = entity.getRestitution() / 2;

        Vector3f oPosition = other.getPosition();
        float oScale = other.getScale() / 2;
        float oRestitution = other.getRestitution();

        float xAxis = axisCollisionControl(
                ePosition.getX(),
                eScale + eRestitution,
                oPosition.getX() - oScale - oRestitution,
                oPosition.getX() + oScale + oRestitution
        );

        float yAxis = axisCollisionControl(
                ePosition.getY(),
                eScale + eRestitution,
                oPosition.getY() - oScale - oRestitution,
                oPosition.getY() + oScale + oRestitution
        );

        float zAxis = axisCollisionControl(
                ePosition.getZ(),
                eScale + eRestitution,
                oPosition.getZ() - oScale - oRestitution,
                oPosition.getZ() + oScale + oRestitution
        );

        if (xAxis != ePosition.getX() && yAxis != ePosition.getY() && zAxis != ePosition.getZ())
            return new Vector3f(xAxis, yAxis, zAxis);
        return ePosition;
    }

    private static float axisCollisionControl(float point, float padding, float min, float max) {
        if ((point + padding <= min) || (point + padding >= max))
            return point;
        else {
            if (Math.abs(point+padding-min) < Math.abs(max - point+padding))
                return min;
            return max;
        }
    }
}
