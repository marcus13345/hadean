package xyz.valnet.engine.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import xyz.valnet.engine.math.Matrix4f;
import xyz.valnet.engine.math.Vector3f;
import xyz.valnet.engine.math.Vector4f;

public class Shader {
	
	private boolean enabled = false;
	
	public final int handle;

	public final static int POSITION = 0;

	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertPath, String fragPath) {
		handle = load(vertPath, fragPath);
	}

	private static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public int load(String vertPath, String fragPath) {
		String vert = Shader.loadAsString(vertPath); 
		String frag = Shader.loadAsString(fragPath);
		return create(vert, frag);
	}
	
	public int create(String vert, String frag) {
		int program = glCreateProgram(); 
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertID, vert);
		glShaderSource(fragID, frag);
		
		glCompileShader(vertID);
		if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(glGetShaderInfoLog(vertID));
			return -1;
		}
		
		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(glGetShaderInfoLog(fragID));
			return -1;
		}
		
    bindAttributes(program);

		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		glValidateProgram(program);
		
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		
		return program;
	}
	
	protected void bindAttributes(int program) {
		glBindAttribLocation(program, POSITION, "Position");
  }

  public int getUniform(String name) {
		if (locationCache.containsKey(name))
			return locationCache.get(name);
		
		int result = glGetUniformLocation(handle, name);
		if (result == -1) 
			System.err.println("Could not find uniform variable '" + name + "'!");
		else
			locationCache.put(name, result);
		return result;
	}
	
	public void setUniform1i(String name, int value) {
		if (!enabled) enable();
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		if (!enabled) enable();
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y) {
		if (!enabled) enable();
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		if (!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniform4f(String name, Vector4f vector) {
		if (!enabled) enable();
		glUniform4f(getUniform(name), vector.x, vector.y, vector.z, vector.w);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}

  public void setMatrices (Matrix4f projection) {
      setUniformMat4f("uProjection", projection);
  }
	
	public void enable() {
		glUseProgram(handle);
		enabled = true;
	}
	
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}

}
