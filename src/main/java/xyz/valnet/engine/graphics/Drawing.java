package xyz.valnet.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import xyz.valnet.engine.shaders.SimpleShader;

public class Drawing {

    private static Texture bound = null;

    public static void drawSprite(Sprite sprite, int x, int y) {
        drawSprite(sprite, x, y, sprite.width, sprite.height);
    }

    public static void drawSprite(Sprite sprite, int x, int y, int width, int height) {
        // lazy texture binding
        if(bound != sprite.atlas) {
            if(bound != null) bound.unbind();
            sprite.atlas.bind();
        }

        glBegin(GL_QUADS);
            glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x, sprite.sourceBoxUV.y);
            glVertex2f(x, y);

            glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x + sprite.sourceBoxUV.z, sprite.sourceBoxUV.y);
            glVertex2f(x + width, y);

            glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x + sprite.sourceBoxUV.z, sprite.sourceBoxUV.y + sprite.sourceBoxUV.w);
            glVertex2f(x + width, y + height);

            glVertexAttrib2f(SimpleShader.TEX_COORD, sprite.sourceBoxUV.x, sprite.sourceBoxUV.y + sprite.sourceBoxUV.w);
            glVertex2f(x, y + height);
        glEnd();
    }
}