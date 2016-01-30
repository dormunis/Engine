package engine.entities;

import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1, 0, 0);

	private boolean flicker = false;
	private Vector3f flickerIntensity;
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}
	
	public Vector3f getAttenuation(){
		if (flicker) {
			Random random = new Random();
			float originalX = flickerIntensity.getX();
			float originalY = flickerIntensity.getY();
			float originalZ = flickerIntensity.getZ();
			float x = originalX > 0 ? random.nextFloat() % originalX : 0;
			float y = originalY > 0 ? random.nextFloat() % originalY : 0;
			float z = originalZ > 0 ? random.nextFloat() % originalZ : 0;
			return new Vector3f(attenuation.getX() + x, attenuation.getY() + y, attenuation.getZ() + z);
		}
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public boolean isFlickering() {
		return flicker;
	}

	public void enableFlicker(boolean flicker, Vector3f flickerIntensity) {
		this.flicker = flicker;
		this.flickerIntensity = flickerIntensity;
	}

	public void disableFlicker() {
		this.flicker = false;
	}

	public Vector3f getFlickerIntensity() {
		return flickerIntensity;
	}

	public void setFlickerIntensity(Vector3f flickerIntensity) {
		this.flickerIntensity = flickerIntensity;
	}
}
