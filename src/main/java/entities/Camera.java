package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private static final int CAMERA_HEIGHT = 0;
	
	private float distanceFromPlayer = 40;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	
	private Player player;

	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		calculateYaw();
	}

	public void invertPitch(){
		this.pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Player getPlayer() {
		return player;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = player.getRotY();
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + CAMERA_HEIGHT;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch+4)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch+4)));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.03f;
		distanceFromPlayer -= zoomLevel;
		if(distanceFromPlayer<5){
			distanceFromPlayer = 5;
		}
	}

	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float XangleChange = calculateYRotation();
			player.increaseRotation(0,-XangleChange,0);
		}
	}

	private void calculateYaw() {
		this.yaw = 180 - player.getRotY();
		yaw %= 360;
	}

    private float calculateYRotation() {
        float pitchChange = Mouse.getDY() * 0.2f;
        pitch -= pitchChange;
        if(pitch < -90){
            pitch = -90;
        }else if(pitch > 90){
            pitch = 90;
        }
        return Mouse.getDX() * 0.3f;
    }
}
