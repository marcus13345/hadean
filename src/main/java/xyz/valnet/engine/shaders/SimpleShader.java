package xyz.valnet.engine.shaders;

import java.util.Stack;

import static org.lwjgl.opengl.GL20.*;

import xyz.valnet.engine.math.Vector4f;

public class SimpleShader extends Shader {

  private Stack<Vector4f> colorStack = new Stack<Vector4f>();

	public final static int COLOR = 1;
	public final static int TEX_COORD = 2;

  public SimpleShader(String vertPath, String fragPath) {
    super(vertPath, fragPath);
  }

  public void pushColor(Vector4f color) {
    // System.out.println("pushing " + color + " onto color stack");
    colorStack.push(color);
    // printColorStack();

    setUniform4f("uColor", color);
  }

  public void swapColor(Vector4f color) {
    popColor();
    pushColor(color);
  }
  
  public void popColor() {
    // System.out.println("popping the color stack");
    colorStack.pop();
    Vector4f newColor = colorStack.peek();
    // printColorStack();

    if(newColor == null) {
      setUniform4f("uColor", Vector4f.one);
      return;
    }
    setUniform4f("uColor", newColor);
  }

  // private void printColorStack() {
  //   for(Vector4f color : colorStack) {
  //     System.out.println("  " + color);
  //   }
  //   System.out.println("");
  // }
  
  @Override
  protected void bindAttributes(int program) {
    glBindAttribLocation(program, COLOR, "Color");
    glBindAttribLocation(program, TEX_COORD, "TexCoord");
  }

}
