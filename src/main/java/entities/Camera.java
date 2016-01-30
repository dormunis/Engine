package entities;

import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class Camera {

	private static final float MINIMUM_PITCH = 5;
	private static final float MAXIMUM_PITCH = 100;

	private static final float MINIMUM_ZOOM = 10;
	private static final float MAXIMUM_ZOOM = 150;

	private boolean invertedCamera = false; // boolean, 1
	private boolean invertedZoom = false; // boolean, 1
	private float zoomIntensityModifier = 0.1f;

	private Vector3f position = new Vector3f(0,0,0);
	private float zoom = 50;

	private float roll = 0; // roll = rotation around the x axis
	private float pitch = 20; // pitch = rotation around the y axis
	private float yaw = 0; // yaw = rotation around the z axis

	private float pitchChangeModifier = 0.1f;
	private float angleChangeModifier = 0.3f;

	private float angleAroundPlayer;

	private boolean offsetCamera = false;
	private Player player;

	public Camera(Player player) { this.player = player; }

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
//		position.x = player.getPosition().getX() - (offsetX * (offsetCamera ? 1 : 0));
		position.y = player.getPosition().getY() + verticalDistance;
//		position.z = player.getPosition().getZ() - (offsetZ * (offsetCamera ? 1 : 0));
		position.z = player.getPosition().getZ() - offsetZ;
	}

	private void calculateYaw() { yaw = 180 - (player.getRotY() + angleAroundPlayer); }
	private float calculateHorizontalDistance() { return (float) (zoom * Math.cos(Math.toRadians(pitch))); }
	private float calculateVerticalDistance() { return (float) (zoom * Math.sin(Math.toRadians(pitch))); }

	private void increasePitch(int delta) {
		pitch = Maths.constrain(pitch + (delta * pitchChangeModifier) * (invertedCamera ? 1 : -1), MINIMUM_PITCH, MAXIMUM_PITCH);
	}
	private void increaseAngleAroundPlayer(int delta) {
		angleAroundPlayer -= (delta * angleChangeModifier);
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
	public float getPitchChangeModifier() { return pitchChangeModifier; }
	public void setPitchChangeModifier(float pitchChangeModifier) { this.pitchChangeModifier = pitchChangeModifier; }
	public float getAngleChangeModifier() { return angleChangeModifier; }
	public void setAngleChangeModifier(float angleChangeModifier) { this.angleChangeModifier = angleChangeModifier; }
	public boolean isInvertedCamera() { return invertedCamera; }
	public boolean isOffsetCamera() { return offsetCamera; }
	public void setOffsetCamera(boolean offsetCamera) { this.offsetCamera = offsetCamera; }
}
