package engine.shaders;

import boot.Main;
import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = Main.fileHandler.getShader("guiVertexShader");
	private static final String FRAGMENT_FILE = Main.fileHandler.getShader("guiFragmentShader");
	
	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	
	

}
