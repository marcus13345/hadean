package xyz.valnet.hadean.gameobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.Tile;
import xyz.valnet.hadean.pathfinding.AStarPathfinder;
import xyz.valnet.hadean.pathfinding.IPathfinder;
import xyz.valnet.hadean.pathfinding.Node;
import xyz.valnet.hadean.pathfinding.Path;
import xyz.valnet.hadean.scenes.GameScene;
import xyz.valnet.hadean.util.Assets;

public class Pawn extends GameObject {

  private float x = 0.5f, y = 0.5f;
  private float dx, dy;

  private float counter = 0;

  private Path path;

  private final float speed = 50f;

  private Camera camera;
  private Terrain terrain;
  private IPathfinder pathfinder;

  @Override
  public void start() {
    camera = get(Camera.class);
    terrain = get(Terrain.class);
    pathfinder = new AStarPathfinder(terrain);
  }

  @Override
  public void render() {
    
    Drawing.setLayer(3f);

    if(path != null && !path.isComplete()) {
      Node next = path.peek();
      float t = counter / speed;
      camera.draw(Assets.pawn, lerp(x - 0.5f, next.x, t), lerp(y - 0.5f, next.y, t));
      
      if(path != null) {
        for(Node node : path) {
          glBegin(GL_LINES);
            Vector2f u, v;

            if(node.from == null) u = camera.world2screen(x, y);
            else u = camera.world2screen(node.from.x + 0.5f, node.from.y + 0.5f);
            
            v = camera.world2screen(node.x + 0.5f, node.y + 0.5f);
            glVertex2f(u.x, u.y);
            glVertex2f(v.x, v.y);
          glEnd();
        }
      }
    } else {
      camera.draw(Assets.pawn, x - 0.5f, y - 0.5f);
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
    if(path != null && !path.isComplete()) move();
    else newPath();
  }

  private void newPath() {
    // set new destination
    dx = 0.5f + (float)Math.floor(Math.random() * Terrain.WORLD_SIZE);
    dy = 0.5f + (float)Math.floor(Math.random() * Terrain.WORLD_SIZE);

    // and route there.
    route();
  }

  private void route() {
    // intify all the coordinates
    int ix = (int)Math.floor(x);
    int iy = (int)Math.floor(y);

    int idx = (int)Math.floor(dx);
    int idy = (int)Math.floor(dy);

    // try to make a new path.
    path = pathfinder.getPath(ix, iy, idx, idy);
  }

  private void move() {
    // check if we CAN move.
    Node nextNode = path.peek();
    Tile nextTile = terrain.getTile(nextNode.x, nextNode.y);
    if(!nextTile.isWalkable()) {
      if(counter > 0) counter --;
      if(counter < 0) counter = 0;
      if(counter == 0) {
        route();
      }
      return;
    }

    counter ++;
    if(counter < speed) return;

    path.pop();
    x = nextNode.x + 0.5f;
    y = nextNode.y + 0.5f;
    counter = 0;
  }
  
}
