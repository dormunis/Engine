package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

import java.util.List;

public class Player extends Entity {

	public static final float RUN_SPEED = 40;
	public static final float TURN_SPEED = 160;
	public static final float GRAVITY = 0;
//	private static final float JUMP_POWER = 18;

	public float currentSpeed = 0;
	public float currentTurnSpeed = 0;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation,
				  float scale) {
		super(model, position, rotation, scale);
	}

	public void move(List<Terrain> terrains, List<Vector3f> constraints) {
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dy = 0;
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));

//		Vector3f increasePositionVector = manageCollisions(constraints, dx, dy, dz);

		super.increasePosition(dx,dy,dz);
//		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
//		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
//		float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
//		if (super.getPosition().y < terrainHeight) {
//			upwardsSpeed = 0;
//			isInAir = false;
//			super.getPosition().y = terrainHeight;
//		}
	}

	private Vector3f manageCollisions(List<Vector3f> constraints, float dx, float dy, float dz) {
		return null;
	}

}
