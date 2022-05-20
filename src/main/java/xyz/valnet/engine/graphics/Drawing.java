package xyz.valnet.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import xyz.valnet.engine.shaders.SimpleShader;

public class Drawing {

  private static Texture bound = null;

  public static void drawSprite(Sprite sprite, int x, int y) {
    drawSprite(sprite, x, y, sprite.width, sprite.height);
  }

  private static float layer = 0f;

  public static void setLayer(float layer) {
    Drawing.layer = layer;
  }

  public static void drawSprite(Sprite sprite, int x, int y, int w, int h) {
    drawSprite(sprite, x, y, w, h, 0);
  }

  public static void drawSprite(Sprite sprite, int x, int y, int width, int height, float lift) {
    // lazy texture binding
    if(bound != sprite.atlas) {
        if(bound != null) bound.unbind();
        sprite.atlas.bind();
    }

    glBegin(GL_QUADS);
      glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x, sprite.sourceBoxUV.y);
      glVertex3f(x, y, layer + height * lift);

      glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x + sprite.sourceBoxUV.z, sprite.sourceBoxUV.y);
      glVertex3f(x + width, y, layer + height * lift);

      glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x + sprite.sourceBoxUV.z, sprite.sourceBoxUV.y + sprite.sourceBoxUV.w);
      glVertex3f(x + width, y + height, layer);

      glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x, sprite.sourceBoxUV.y + sprite.sourceBoxUV.w);
      glVertex3f(x, y + height, layer);
    glEnd();
  }
}