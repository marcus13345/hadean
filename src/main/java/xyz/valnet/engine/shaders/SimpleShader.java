package xyz.valnet.engine.shaders;

import static org.lwjgl.opengl.GL20.*;

import java.util.Stack;

import xyz.valnet.engine.graphics.Color;

public class SimpleShader extends Shader {

  private Stack<Color> colorStack = new Stack<Color>();

	public final static int COLOR = 1;
	public final static int TEX_COORD = 2;

  public SimpleShader(String vertPath, String fragPath) {
    super(vertPath, fragPath);
  }

  public void pushColor(Color color) {
    colorStack.push(color);
    setUniform4f("uColor", color);
  }

  public void swapColor(Color color) {
    popColor();
    pushColor(color);
  }

  public void clearColorStack() {
    colorStack.clear();
  }
  
  public void popColor() {
    colorStack.pop();
    Color newColor = colorStack.peek();

    if(newColor == null) {
      setUniform4f("uColor", Color.white);
      return;
    }
    setUniform4f("uColor", newColor);
  }
  
  @Override
  protected void bindAttributes(int program) {
    glBindAttribLocation(program, COLOR, "Color");
    glBindAttribLocation(program, TEX_COORD, "TexCoord");
  }

}
