package xyz.valnet.hadean.gameobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL20.glVertexAttrib2f;
import static xyz.valnet.engine.util.Math.lerp;

import java.util.Comparator;
import java.util.List;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.shaders.SimpleShader;
import xyz.valnet.hadean.Tile;
import xyz.valnet.hadean.pathfinding.AStarPathfinder;
import xyz.valnet.hadean.pathfinding.IPathfinder;
import xyz.valnet.hadean.pathfinding.Node;
import xyz.valnet.hadean.pathfinding.Path;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class Pawn extends GameObject implements ISelectable {

  private float x = 0.5f, y = 0.5f;

  private float counter = 0;

  private Path path;

  private final float invocationThreshold = 50 + (float)(Math.random() * 20);

  private Camera camera;
  private Terrain terrain;
  private IPathfinder pathfinder;

  private boolean debug = false;

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
      float t = counter / invocationThreshold;
      camera.draw(Assets.pawn, lerp(x - 0.5f, next.x, t), lerp(y - 0.5f, next.y, t));
      
      if(path != null && debug) {
        for(Node node : path) {
          glBegin(GL_LINES);
            Vector2f u, v;
            
            if(node.from == null) u = camera.world2screen(x, y);
            else u = camera.world2screen(node.from.x + 0.5f, node.from.y + 0.5f);
            
            v = camera.world2screen(node.x + 0.5f, node.y + 0.5f);
            glVertexAttrib2f(SimpleShader.TEX_COORD, 0, 88 / 256f);
            glVertex3f(u.x, u.y, 3f);
            glVertexAttrib2f(SimpleShader.TEX_COORD, 0, 88 / 255f);
            glVertex3f(v.x, v.y, 3f);
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

    // then, try to do work!
    if(currentJob != null && currentJob.hasWork()) {
      if(getCurrentPos().isOneOf(currentJob.getWorablePositions())) {
        currentJob.doWork();
        return;
      }
    }

    // firstly, TRY PATHING.
    if(path != null && !path.isComplete()) {
      move();
    }

    // then try to get work?!
    if(counter == 0) {
      currentJob = null;
      tryStartWork();
      

      if(currentJob == null && (path == null || path.isComplete())) {
        // then wander...
        newPath();
      }
    }

  }

  private Vector2i getCurrentPos() {
    return new Vector2i((int)Math.floor(x), (int)Math.floor(y));
  }

  private IWorkable currentJob;

  private void tryStartWork() {
    List<IWorkable> workables = getAll(IWorkable.class);

    workables.sort(new Comparator<IWorkable>() {
      @Override
      public int compare(IWorkable a, IWorkable b) {
        float distA = a.getLocation().distanceTo((int)x, (int)y);
        float distB = b.getLocation().distanceTo((int)x, (int)y);
        if(distA > distB) return -1;
        if(distB > distA) return 1;
        return 0;
      }
    });

    if(workables.size() > 0) {
      for(IWorkable job : workables) {
        if(!job.hasWork()) continue;
        Vector2i[] workablePositions = job.getWorablePositions();
        Path bestPathToJob = pathfinder.getBestPath(
          new Vector2i((int)Math.floor(x), (int)Math.floor(y)),
          workablePositions
        );
        if(bestPathToJob == null) continue;
        this.path = bestPathToJob;
        currentJob = job;
      }
    }
  }

  private void newPath() {
    // set new destination
    int randomX = (int)Math.floor(Math.random() * Terrain.WORLD_SIZE);
    int randomY = (int)Math.floor(Math.random() * Terrain.WORLD_SIZE);
    path = pathfinder.getPath((int)x, (int)y, randomX, randomY);

    // // and route there.
    // reroute();
  }

  private void reroute() {
    // intify all the coordinates
    int ix = (int)Math.floor(x);
    int iy = (int)Math.floor(y);

    int idx = path.dst.x;
    int idy = path.dst.y;

    // try to make a new path.
    path = pathfinder.getPath(ix, iy, idx, idy);
  }

  private boolean paused = false;

  private void move() {
    if(paused) {
      counter --;
      if(counter < 0) counter = 0;
      return;
    }
    // check if we CAN move.
    Node nextNode = path.peek();
    Tile nextTile = terrain.getTile(nextNode.x, nextNode.y);
    if(!nextTile.isWalkable()) {
      if(counter > 0) counter --;
      if(counter < 0) counter = 0;
      if(counter == 0) {
        reroute();
      }
      return;
    }

    counter ++;
    if(counter < invocationThreshold) return;

    path.pop();
    x = nextNode.x + 0.5f;
    y = nextNode.y + 0.5f;
    counter = 0;
  }

  @Override
  public Vector4f getWorldBox() {
    if(path != null && !path.isComplete()) {
      float t = counter / invocationThreshold;
      Node n = path.peek();
      float x1 = lerp(x - 0.5f, n.x, t);
      float y1 = lerp(y - 0.5f, n.y, t);
      return new Vector4f(x1, y1, x1 + 1, y1 + 1);
    } else {
      return new Vector4f(x - 0.5f, y - 0.5f, x + 0.5f, y + 0.5f);
    }
  }

  private static final Action ACTION_REROUTE = new Action("Re-route");
  private static final Action ACTION_TOGGLE_DEBUG = new Action("Toggle\nDebug");
  private static final Action ACTION_PAUSE = new Action("Pause");

  @Override
  public Action[] getActions() {
    return new Action[] {
      // ACTION_REROUTE,
      ACTION_TOGGLE_DEBUG,
      ACTION_PAUSE
    };
  }

  @Override
  public void runAction(Action action) {
    if(action == ACTION_PAUSE) {
      paused = !paused;
    } else if(action == ACTION_REROUTE) {
      reroute();
    } else if(action == ACTION_TOGGLE_DEBUG) {
      debug = !debug;
    }
  }

  @Override
  public String details() {
    return "IM A PAWNNNNN!!!!";
  }
  
}
