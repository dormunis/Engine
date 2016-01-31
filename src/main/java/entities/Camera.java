package entities;

import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class Camera {

	private static final float MINIMUM_PITCH = -90;
	private static final float MAXIMUM_PITCH = 90;

	private static final float MINIMUM_ZOOM = 10;
	private static final float MAXIMUM_ZOOM = 150;

	private static final float ANGLE_BUFFER_DECREASE_RATIO = 0.1f;

	private boolean invertedCamera = false; // boolean, 1
	private boolean invertedZoom = false; // boolean, 1
	private boolean lockToPlayer = true;

	private Vector3f position = new Vector3f(0,0,0);
	private float zoom = 50;
	private float angleAroundPlayer;
	private float angleBuffer = 0;
	private float currentAngleBufferDecreaseConstant = 0;

	private float roll = 0; // roll = rotation around the x axis
	private float pitch = 20; // pitch = rotation around the y axis
	private float yaw = 0; // yaw = rotation around the z axis

	private float mouseSensitivity = 0.4f;
	private float zoomIntensityModifier = 0.1f;

	private Player player;

	public Camera(Player player) { this.player = player; }

	public Camera(Player player, boolean lockToPlayer) {
		this.player = player;
		this.lockToPlayer = lockToPlayer;
	}

	public void move() {
		calculateYaw();
		calculateCameraPosition(calculateHorizontalDistance(), calculateVerticalDistance());

	}

	public void storeInput(int dx, int dy, int dz, int zoom) {
		increaseAngleAroundPlayer(dx);
		increasePitch(dy);
		increaseZoom(zoom);
	}

	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

		position.x = player.getPosition().getX() - offsetX;
		position.y = player.getPosition().getY() + verticalDistance;
		position.z = player.getPosition().getZ() - offsetZ;
	}

	private void calculateYaw() {
		if ((currentAngleBufferDecreaseConstant > 0 && angleBuffer > 0) || (currentAngleBufferDecreaseConstant < 0 && angleBuffer < 0)) {
			angleBuffer -= currentAngleBufferDecreaseConstant;
			angleAroundPlayer -= currentAngleBufferDecreaseConstant;
		}
		else
			currentAngleBufferDecreaseConstant = 0;
		yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	private float calculateHorizontalDistance() { return (float) (zoom * Math.cos(Math.toRadians(pitch))); }
	private float calculateVerticalDistance() { return (float) (zoom * Math.sin(Math.toRadians(pitch))); }

	private void increasePitch(int delta) {
		float pitchChange = (delta * mouseSensitivity) * (invertedCamera ? 1 : -1);
//		if (lockToPlayer)
//			player.increaseRotation(pitchChange,0,0);
//		else
			pitch = Maths.constrain(pitch + pitchChange, MINIMUM_PITCH, MAXIMUM_PITCH);
	}
	private void increaseAngleAroundPlayer(int delta) {
		float angleChange = delta * mouseSensitivity;
		if (lockToPlayer)
			player.increaseRotation(0,-angleChange,0);
		else
			angleAroundPlayer = angleAroundPlayer - angleChange;
	}
	private void increaseZoom(int delta) { zoom = Maths.constrain(zoom + ((delta * zoomIntensityModifier) * (invertedZoom ? 1 : -1)), MINIMUM_ZOOM, MAXIMUM_ZOOM); }

	public Vector3f getPosition() { return position; }
	public float getPitch() { return pitch; }
	public float getYaw() { return yaw;	}
	public float getRoll() { return roll; }
	public boolean isInvertedZoom() { return invertedZoom; }
	public void setInvertedZoom(boolean invertedZoom) { this.invertedZoom = invertedZoom; }
	public float getZoomIntensityModifier() { return zoomIntensityModifier; }
	public void setZoomIntensityModifier(float zoomIntensityModifier) { this.zoomIntensityModifier = zoomIntensityModifier;	}
	public float getMouseSensitivity() { return mouseSensitivity; }
	public void setMouseSensitivity(float mouseSensitivity) { this.mouseSensitivity = mouseSensitivity; }
	public boolean isInvertedCamera() { return invertedCamera; }
	public boolean isLockedToPlayer() { return lockToPlayer; }
	public void unlockFromPlayer() { this.lockToPlayer = false;	}
	public void lockToPlayer() {
		if (!lockToPlayer) {
			angleBuffer = angleAroundPlayer % 360;
			currentAngleBufferDecreaseConstant = angleBuffer * ANGLE_BUFFER_DECREASE_RATIO;
		}
		this.lockToPlayer = true;
	}
}
