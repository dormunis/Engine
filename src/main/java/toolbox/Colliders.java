package toolbox;

import entities.Entity;
import entities.Player;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by dor on 29/01/16.
 */
public class Colliders {

    public static Vector3f checkCollision(Vector3f oldPos, Vector3f newPos) {
        Vector3f collisionVector = new Vector3f(1,1,1);
        Vector3f movementVector = Vector3f.sub(newPos, oldPos, null);

        if (movementVector.length() > 0) {

        }

        return collisionVector;
    }

    public static boolean intersectsWith(Vector3f boxMin, Vector3f boxMax, Vector3f spherePosition, float sphereRadius) {
        float dmin = 0;

        if (spherePosition.x < boxMin.x) {
            dmin += Math.pow(spherePosition.x - boxMin.x, 2);
        } else if (spherePosition.x > boxMax.x) {
            dmin += Math.pow(spherePosition.x - boxMax.x, 2);
        }

        if (spherePosition.y < boxMin.y) {
            dmin += Math.pow(spherePosition.y - boxMin.y, 2);
        } else if (spherePosition.y > boxMax.y) {
            dmin += Math.pow(spherePosition.y - boxMax.y, 2);
        }

        if (spherePosition.z < boxMin.z) {
            dmin += Math.pow(spherePosition.z - boxMin.z, 2);
        } else if (spherePosition.z > boxMax.z) {
            dmin += Math.pow(spherePosition.z - boxMax.z, 2);
        }

        return dmin <= Math.pow(sphereRadius, 2);
    }

    public static Vector3f collisionResponse(Player player, Entity entity) {
        Vector3f offset = closestPointOnBox(player.getPosition(), player.getRadius(), entity.getPosition(), new Vector3f(4,4,4));
        offset.normalise();
        return offset;
    }

    private static Vector3f closestPointOnBox(Vector3f point, float radius, Vector3f center, Vector3f extent)
    {
        Vector3f delta = Vector3f.sub(point, center, null);
        extent = Vector3f.add(extent,new Vector3f(radius,radius,radius),null);
        boolean inside = true;

        if (Math.abs(delta.x) > extent.x) {
            inside = false;
            delta.x = extent.x * Math.signum(delta.x);
        }
        if (Math.abs(delta.y) > extent.y) {
            inside = false;
            delta.y = extent.y * Math.signum(delta.y);
        }
        if (Math.abs(delta.z) > extent.z) {
            inside = false;
            delta.z = extent.z * Math.signum(delta.z);
        }

        if (!inside)
//            return Vector3f.add(center, delta, null);
                return delta;

        // calculate the minimum translation distance of the point from one face along each axis
        Vector3f minTranslationDistance = new Vector3f(
                (extent.x - Math.abs(delta.x)) * Math.signum(delta.x),
                (extent.y - Math.abs(delta.y)) * Math.signum(delta.y),
                (extent.z - Math.abs(delta.z)) * Math.signum(delta.z)
        );

        // find the minimum of the three
        if (Math.abs(minTranslationDistance.x) < Math.abs(minTranslationDistance.y)) {
            minTranslationDistance.y = 0.0f;
        if (Math.abs(minTranslationDistance.z) < Math.abs(minTranslationDistance.x))
            minTranslationDistance.x = 0.0f;
        else
            minTranslationDistance.z = 0.0f;
        }
        else {
            minTranslationDistance.x = 0.0f;

            if (Math.abs(minTranslationDistance.z) < Math.abs(minTranslationDistance.y))
                minTranslationDistance.y = 0.0f;
            else
                minTranslationDistance.z = 0.0f;
        }
        // point on surface
        return Vector3f.add(point, minTranslationDistance, null);
    }


}
