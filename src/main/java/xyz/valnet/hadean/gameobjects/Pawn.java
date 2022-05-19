package xyz.valnet.hadean.gameobjects;

import static xyz.valnet.engine.util.Math.lerp;

import java.util.Stack;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.pathfinding.Node;
import xyz.valnet.hadean.scenes.GameScene;
import xyz.valnet.hadean.util.Assets;

import static org.lwjgl.opengl.GL20.*;
// import org.lwjgl.opengl.GL20;

public class Pawn extends GameObject {

  private float x, y;
  private float dx, dy;

  private float counter = 0;

  private Stack<Node> path;

  private final float speed = 20f;

  public Pawn(GameScene scene) {
    super(scene);
  }

  @Override
  public void render() {
    
    Drawing.setLayer(0.15f);

    if(path != null && path.size() > 0) {
      float cx = (int)(Terrain.left + (x - 0.5f) * Terrain.TILE_SIZE);
      float cy = (int)(Terrain.top  + (y - 0.5f) * Terrain.TILE_SIZE);

      Node n = path.peek();

      float nx = Terrain.left + n.x * Terrain.TILE_SIZE;
      float ny = Terrain.top + n.y * Terrain.TILE_SIZE;

      System.out.println("" + n.x + " " + n.y);
  
      Drawing.drawSprite(Assets.pawn, (int)lerp(cx, nx, counter / speed), (int)lerp(cy, ny, counter / speed));
      
      if(path != null) {
        for(Node node : path) {
          glBegin(GL_LINES);
            if(node.from == null) {
              glVertex2f(Terrain.left + x * Terrain.TILE_SIZE, Terrain.top + y * Terrain.TILE_SIZE);
            } else {
              glVertex2f(Terrain.left + (node.from.x + 0.5f) * Terrain.TILE_SIZE, Terrain.top + (node.from.y + 0.5f) * Terrain.TILE_SIZE);
            }
            glVertex2f(Terrain.left + (node.x + 0.5f) * Terrain.TILE_SIZE, Terrain.top + (node.y + 0.5f) * Terrain.TILE_SIZE);
          glEnd();
        }
      }
    } else {
      int nx = (int)(Terrain.left + (x - 0.5f) * Terrain.TILE_SIZE);
      int ny = (int)(Terrain.top  + (y - 0.5f) * Terrain.TILE_SIZE);
  
      Drawing.drawSprite(Assets.pawn, nx, ny);
    }
    
    // Drawing.setLayer(0.1f);
    // Assets.flat.pushColor(Vector4f.black);
    // Drawing.drawSprite(Assets.pawn, (int)(Terrain.left + dx * Terrain.TILE_SIZE) - Assets.pawn.width / 2, (int)(Terrain.top + dy * Terrain.TILE_SIZE) - Assets.pawn.height / 2);
    
    // Assets.flat.swapColor(new Vector4f(1, 0, 0, 1));
    // Drawing.setLayer(0.05f);

    // Assets.flat.popColor();
  }

  @Override
  public void tick(float dTime) {
    counter ++;
    if(counter >= speed) action();
  }



  private void action() {
    if(path == null || path.empty()) {
      dx = 0.5f + (float)Math.floor(Math.random() * Terrain.WORLD_SIZE);
      dy = 0.5f + (float)Math.floor(Math.random() * Terrain.WORLD_SIZE);

      int ix = (int)Math.floor(x);
      int iy = (int)Math.floor(y);

      int idx = (int)Math.floor(dx);
      int idy = (int)Math.floor(dy);

      path = get(Terrain.class).getPath(ix, iy, idx, idy);
      if(path != null) {
        counter = 0;
      }
    } else {
      Node n = path.pop();
      x = n.x + 0.5f;
      y = n.y + 0.5f;
      counter = 0;
    }

  }
  
}
