package xyz.valnet.hadean.gameobjects.worldobjects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL20.glVertexAttrib2f;
import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.shaders.SimpleShader;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.interfaces.IHaulable;
import xyz.valnet.hadean.interfaces.IJob;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.interfaces.IWorker;
import xyz.valnet.hadean.pathfinding.AStarPathfinder;
import xyz.valnet.hadean.pathfinding.IPathfinder;
import xyz.valnet.hadean.pathfinding.Node;
import xyz.valnet.hadean.pathfinding.Path;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Pawn extends WorldObject implements ISelectable, IWorker {

  private static int count = 0;
  private String name = "Pawn " + (++ count);

  private JobBoard jobboard;
  private IPathfinder pathfinder;

  private IHaulable carrying = null;

  private float counter = 0;

  private Path path;

  private final float invocationThreshold = 100 + (float)(Math.random() * 50);

  private boolean debug = false;

  @Override
  public void start() {
    super.start();
    jobboard = get(JobBoard.class);
    pathfinder = new AStarPathfinder(terrain);
    x = 0.5f + (int) (Math.random() * Terrain.WORLD_SIZE);
    y = 0.5f + (int) (Math.random() * Terrain.WORLD_SIZE);
  }

  @Override
  public void selectedRender() {
    if(path != null) {
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
  }

  @Override
  public void render() {

    if(path != null && !path.isComplete()) {
      Node next = path.peek();
      float t = counter / invocationThreshold;
      camera.draw(Layers.PAWNS, Assets.pawn, lerp(x - 0.5f, next.x, t), lerp(y - 0.5f, next.y, t));
      
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
      camera.draw(Layers.PAWNS, Assets.pawn, x - 0.5f, y - 0.5f);
    }
    
    // Drawing.setLayer(0.1f);
    // Assets.flat.pushColor(Vector4f.black);
    // Drawing.drawSprite(Assets.pawn, (int)(Terrain.left + dx * Terrain.TILE_SIZE) - Assets.pawn.width / 2, (int)(Terrain.top + dy * Terrain.TILE_SIZE) - Assets.pawn.height / 2);
    
    // Assets.flat.swapColor(new Vector4f(1, 0, 0, 1));
    // Drawing.setLayer(0.05f);

    // Assets.flat.popColor();
  }

  @Override
  public void update(float dTime) {

    IJob currentJob = jobboard.getJob(this);

    // cleanup current job...
    if(currentJob != null && !currentJob.hasWork()) {
      currentJob = null;
    }

    // if you dont have a job
    if(currentJob == null && carrying == null) {
      // and its a frame to try and get one...
      if(counter == 0) {
        tryStartWork();
        currentJob = jobboard.getJob(this);
      }
      
      // if we still dont have a job, try path to wander.
      if(currentJob == null && (path == null || path.isComplete())) {
        newPath();
        return;
      }
      
      // TODO possibly take care of needs here idk
    }

    if(path != null && !path.isComplete()) {
      move();
      return;
    }

    // try to do your work!
    if(currentJob != null && currentJob.hasWork()) {
      if(getCurrentPos().isOneOf(currentJob.getWorablePositions())) {
        if(currentJob instanceof IWorkable) {
          ((IWorkable)currentJob).doWork();
        } else if (currentJob instanceof IHaulable) {
          if(carrying == null) {
            IHaulable thing = (IHaulable) currentJob;
            Log log = thing.take();
            carrying = log;
            Vector2i dst = thing.getDestination().getCoords();
            path = pathfinder.getPath((int)x, (int)y, dst.x, dst.y);
          }
        }
        return;
      }
    } else if (carrying != null) {
      // if we're at our destination, or if for some reason we just like
      // didnt make it? but our path is so totally completed...
      if(carrying.getDestination() == this.getTile() || path == null || path.isComplete()) {
        this.getTile().placeThing((ITileThing) carrying);
        carrying = null;
      }
    }

  }

  private Vector2i getCurrentPos() {
    return new Vector2i((int)Math.floor(x), (int)Math.floor(y));
  }

  private void tryStartWork() {
    jobboard.requestJob(this);
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

  private String getCarriedName() {
    if(carrying == null) return "Nothing";
    String[] names = carrying.getClass().getName().split("\\.");
    return names[names.length - 1];
  }

  @Override
  public String details() {
    IJob currentJob = jobboard.getJob(this);
    String jobString = currentJob == null ? "No Job" : currentJob.getJobName();
    return "" + name + "\n" +
           "Held | " + getCarriedName() + "\n" + 
           "Job  | " + jobString + "\n" +
           "";
  }

  @Override
  public Vector2f getLocation() {
    return new Vector2f(x, y);
  }

  @Override
  public IPathfinder getPathfinder() {
    return pathfinder;
  }

  @Override
  public void setPath(Path path) {
    this.path = path;
  }

  @Override
  public String getName() {
    return name;
  }
  
}
