package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;
import terrains.Terrain;
import toolbox.Colliders;
import toolbox.Maths;

import java.util.List;
import java.util.stream.Collectors;

public class Player extends Entity {

	private static final float CLOSENESS_THRESHOLD = 10;

	public static final float VERTICAL_VECTOR_STRENGTH = 1.2f;

	public final float radius = 1;
	public static final float RUN_SPEED = 80;
	public static final float TURN_SPEED = 160;
	public static final float GRAVITY = 0;

	public float currentSpeed = 0;
	public float currentVerticalSpeed = 0;
	public float currentTurnSpeed = 0;

	private Vector3f velocity;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation,
				  float scale) {
		super(model, position, rotation, scale);
	}

	public void move(List<Terrain> terrains, List<Entity> entities) {

		velocity = calculateVelocity();

		List<Entity> relevantEntities = entities.stream().filter(
				e -> Maths.getDistanceBetweenVectors(
						Vector3f.add(getPosition(), velocity, null),
						e.getPosition()
				) < CLOSENESS_THRESHOLD).collect(Collectors.toList());

		for (Entity entity : relevantEntities) {
			Vector3f boxOffset = new Vector3f(1, 1, 1);
			boxOffset.scale(entity.getScale());
			Vector3f min = Vector3f.sub(entity.getPosition(), boxOffset, null);
			Vector3f max = Vector3f.add(entity.getPosition(), boxOffset, null);
			if (Colliders.intersectsWith(min, max, getPosition(), 1) && velocity.length() != 0) {
				velocity = Colliders.collisionResponse(this, entity);
			}
		}
		super.increasePosition(velocity.getX(), velocity.getY(), velocity.getZ());
	}

	private Vector3f calculateVelocity() {
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dy = (float) -(distance * Math.sin(Math.toRadians(super.getRotX())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		return new Vector3f(dx,dy+currentVerticalSpeed,dz);
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public float getRadius() {
		return radius;
	}

	public void storeInput(float dx, float dy, float dz) {
		currentTurnSpeed = dx;
		currentVerticalSpeed = dy;
		currentSpeed = dz;
	}
}
