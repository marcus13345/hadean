package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.Layers;
import xyz.valnet.hadean.Tile;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class Log extends GameObject implements ITileThing, ISelectable, IHaulable {

  private Camera camera;
  private Terrain terrain;

  private int x, y;

  private boolean haul = false;

  public Log(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void start() {
    camera = get(Camera.class);
    terrain = get(Terrain.class);
  }

  @Override
  public void render() {
    Drawing.setLayer(Layers.GROUND);
    camera.draw(Assets.log, x, y);
    if(haul) {
      Drawing.setLayer(Layers.MARKERS);
      camera.draw(Assets.haulArrow, x, y);
    }
  }

  @Override
  public boolean isWalkable() {
    return true;
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {}

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
  }

  private static final Action ACTION_HAUL = new Action("Haul");

  @Override
  public Action[] getActions() {
    return new Action[] {
      ACTION_HAUL
    };
  }

  @Override
  public void runAction(Action action) {
    if(action == ACTION_HAUL) {
      haul = !haul;
    }
  }

  @Override
  public String details() {
    return "A fat log";
  }

  @Override
  public boolean hasWork() {
    return haul;
  }

  @Override
  public Vector2i[] getWorablePositions() {
    return new Vector2i[] {
      new Vector2i(x + 1, y),
      new Vector2i(x - 1, y),
      new Vector2i(x, y + 1),
      new Vector2i(x, y - 1)
    };
  }

  @Override
  public Vector2i getLocation() {
    return new Vector2i(x, y);
  }

  @Override
  public Log take() {
    haul = false;
    Tile tile = terrain.getTile(x, y);
    tile.remove(this);
    return this;
  }

  @Override
  public Tile getDestination() {
    return get(Stockpile.class).getTile();
  }

  @Override
  public void updatePosition(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
}
