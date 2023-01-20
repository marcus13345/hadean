package xyz.valnet.hadean.gameobjects.worldobjects.agents;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL20.glVertexAttrib2f;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.shaders.SimpleShader;
import xyz.valnet.hadean.HadeanGame;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.pathfinding.AStarPathfinder;
import xyz.valnet.hadean.pathfinding.IPathfinder;
import xyz.valnet.hadean.pathfinding.Node;
import xyz.valnet.hadean.pathfinding.Path;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import static xyz.valnet.engine.util.Math.lerp;

public abstract class Agent extends WorldObject implements ISelectable {
  public abstract String getName();
  private float frameCounter = 0;
  private int speed = 100 + (int)(Math.random() * 50);

  private IPathfinder pathfinder;
  private Path path = null;

  private boolean stopPathingFlag = false;

  public boolean isPathing() {
    return path != null && !path.isComplete();
  }

  public Vector2f getCalculatedPosition() {
    if(path == null || path.isComplete()) return getWorldPosition().xy().asFloat();
    Vector2i pos = getWorldPosition().xy();
    Vector2f nextPos = path.peek().getPosition().asFloat();
    return new Vector2f(
      lerp(pos.x, nextPos.x, frameCounter / (float)speed),
      lerp(pos.y, nextPos.y, frameCounter / (float)speed)
    );
  }

  @Override
  public void start() {
    super.start();
    frameCounter = 0;
    pathfinder = new AStarPathfinder(terrain);
  }

  @Override
  public void update(float dTime) {
    think();
    act(dTime);
    postAct();
  }

  protected abstract void postAct();

  private void move(float dTime) {
    frameCounter += dTime;
    if(frameCounter >= speed) {
      Vector2i nextPos = path.pop().getPosition();
      setPosition(nextPos.x, nextPos.y);
      if(nextPath != null) {
        path = nextPath;
        nextPath = null;
      }
      if(path.isComplete()) path = null;
      frameCounter -= speed;
      if(stopPathingFlag) {
        path = null;
        nextPath = null;
        stopPathingFlag = false;
      }
    }
  }

  protected void stopPathing() {
    stopPathingFlag = true;
    nextPath = null;
  }

  private void correctPath() {
    if(path != null && path.isComplete()) path = null;
    if(path == null) return;
    if(path.peek().getPosition().equals(this.getWorldPosition().xy())) {
      path.pop();
    }
    if(path != null && path.isComplete()) path = null;
    if(path == null) return;
    Tile nextTile = terrain.getTile(path.peek().getPosition());
    if(!nextTile.isWalkable()) {
      Vector2i pos = getWorldPosition().xy();
      path = pathfinder.getPath(
        pos.x,
        pos.y,
        path.dst.x,
        path.dst.y
      );
    }
  }

  protected void think() {
    correctPath();
  }

  protected boolean act(float dTime) {
    if(path != null) {
      move(dTime);
      return true;
    }

    return false;
  }

  private Path nextPath = null;

  public void goTo(int x, int y) {
    Vector2i pos = getWorldPosition().xy();
    Path newPath = pathfinder.getPath(pos.x, pos.y, x, y);
    if(path == null) {
      path = newPath;
      frameCounter -= 0;
    } else {
      nextPath = newPath;
    }
  }

  public void goToClosest(Vector2i[] destinations) {
    Path newPath = pathfinder.getBestPath(getWorldPosition().xy(), destinations);
    if(path == null) {
      path = newPath;
      frameCounter = 0;
    } else {
      nextPath = newPath;
    }
  }

  public void goTo(Vector2i location) {
    goTo(location.x, location.y);
  }

  public void wander() {
    Vector2i pos = getWorldPosition().xy();
    int randomX = (int)Math.floor(Math.random() * Terrain.WORLD_SIZE);
    int randomY = (int)Math.floor(Math.random() * Terrain.WORLD_SIZE);
    path = pathfinder.getPath(pos.x, pos.y, randomX, randomY);
  }

  @Override
  public void renderAlpha() {
    if(!HadeanGame.debugView) return;
    Drawing.setLayer(Layers.GROUND_MARKERS);
    Assets.flat.pushColor(Vector4f.opacity(0.6f));
    if(path != null) {
      int count = 0;
      for(Node node : path) {
        glBegin(GL_LINES);
          Vector2i u, v;

          Vector2i pos = getWorldPosition().xy();
          if(node.from == null) u = camera.world2screen(pos.x, pos.y);
          else u = camera.world2screen(node.from.x + 0.5f, node.from.y + 0.5f);

          v = camera.world2screen(node.x + 0.5f, node.y + 0.5f);

          if(count == path.getLength() - 1) {
            u = camera.world2screen(getCalculatedPosition().add(new Vector2f(0.5f, 0.5f)));
          }
          glVertexAttrib2f(SimpleShader.TEX_COORD, 0, 88 / 256f);
          glVertex3f(u.x, u.y, 3f);
          glVertexAttrib2f(SimpleShader.TEX_COORD, 0, 88 / 255f);
          glVertex3f(v.x, v.y, 3f);
        glEnd();
        count ++;
      }

      Assets.selectionFrame.draw(
        camera.world2Screen(
          terrain.getTile(
            path.getDestination().getPosition()
          )
          .getWorldBox()
        )
        .toXYWH()
      );
    }
    Assets.flat.popColor();
  }

  @Override
  public Action[] getActions() {
    return new Action[0];
  }

  @Override
  public ISelectable.Priority getSelectPriority() {
    return ISelectable.Priority.HIGH;
  }

  public Vector2i getDestination() {
    if(nextPath != null) return nextPath.getDestination().getPosition();
    if(path == null) return null;
    return path.getDestination().getPosition();
  }
}
