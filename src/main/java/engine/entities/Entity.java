package engine.entities;

import engine.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	private float mass;
	private float restitution = 0.25f;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotation.getX();
		this.rotY = rotation.getY();
		this.rotZ = rotation.getZ();
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation,
			float scale) {
		this.textureIndex = index;
		this.model = model;
		this.position = position;
		this.rotX = rotation.getX();
		this.rotY = rotation.getY();
		this.rotZ = rotation.getZ();
		this.scale = scale;
	}

    public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	public void increasePosition(Vector3f vector) {
		this.position.x = vector.x;
		this.position.y = vector.y;
		this.position.z = vector.z;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Entity entity = (Entity) o;

		if (Float.compare(entity.rotX, rotX) != 0) return false;
		if (Float.compare(entity.rotY, rotY) != 0) return false;
		if (Float.compare(entity.rotZ, rotZ) != 0) return false;
		if (Float.compare(entity.scale, scale) != 0) return false;
		if (Float.compare(entity.mass, mass) != 0) return false;
		if (textureIndex != entity.textureIndex) return false;
		if (model != null ? !model.equals(entity.model) : entity.model != null) return false;
		return position != null ? position.equals(entity.position) : entity.position == null;

	}

	@Override
	public int hashCode() {
		int result = model != null ? model.hashCode() : 0;
		result = 31 * result + (position != null ? position.hashCode() : 0);
		result = 31 * result + (rotX != +0.0f ? Float.floatToIntBits(rotX) : 0);
		result = 31 * result + (rotY != +0.0f ? Float.floatToIntBits(rotY) : 0);
		result = 31 * result + (rotZ != +0.0f ? Float.floatToIntBits(rotZ) : 0);
		result = 31 * result + (scale != +0.0f ? Float.floatToIntBits(scale) : 0);
		result = 31 * result + (mass != +0.0f ? Float.floatToIntBits(mass) : 0);
		result = 31 * result + textureIndex;
		return result;
	}
}
