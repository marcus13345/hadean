package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.worldobjects.FarmPlot;
import xyz.valnet.hadean.gameobjects.worldobjects.Tree;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Tile extends WorldObject implements IWorkable {

  private Camera camera;

  private final int x, y;
  private final Vector4f color = new Vector4f((float) Math.random() * 0.1f, 0.4f + (float) Math.random() * 0.15f, (float) Math.random() * 0.05f, 1f);
  private final Sprite sprite = Assets.defaultTerrain[(int)Math.floor(Math.random() * Assets.defaultTerrain.length)];

  private List<ITileThing> stuff = new ArrayList<ITileThing>();
  private List<ITileThing> toRemove = new ArrayList<ITileThing>();

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Vector2i getCoords() {
    return new Vector2i(x, y);
  }

  public void start() {
    camera = get(Camera.class);

    if(Math.random() > 0.97) {
      Tree tree = new Tree(x, y);
      stuff.add(tree);
      add(tree);
    }

    // if(Math.random() > 0.98) {
    //   Log log = new Log(x, y);
    //   stuff.add(log);
    //   add(log);
    // }
  }

  public void placeThing(ITileThing thing) {
    stuff.add(thing);
    if(thing instanceof GameObject) {
      add((GameObject)thing);
    }
    if(thing instanceof FarmPlot) {
      desiredTill = true;
      get(JobBoard.class).postJob(this);
    }
  }

  @Override
  public void update(float dTime) {
    for(ITileThing thing : stuff) {
      if(thing.shouldRemove()) {
        toRemove.add(thing);
      }
    }
    for(ITileThing thing : toRemove) {
      stuff.remove(thing);
      thing.onRemove();
      if(thing instanceof GameObject) {
        remove((GameObject)thing);
      }
    }
    toRemove.clear();
  }

  @Override
  public void render() {
    if(tillLevel < 1f) {
      Assets.flat.pushColor(color);
      camera.draw(Layers.TILES, sprite, x, y);
      Assets.flat.popColor();
    }
    if(tillLevel > 0f) {
      Assets.flat.pushColor(Vector4f.opacity(tillLevel));
      camera.draw(Layers.TILES, Assets.farmPlot, x, y);
      Assets.flat.popColor();
    }
  }

  public boolean isWalkable() {
    for(ITileThing thing : stuff) {
      if(!thing.isWalkable()) return false;
    }
    return true;
  }

  private boolean desiredTill = false;
  private float tillLevel = 0;

  public void setTill(boolean till) {
    desiredTill = till;
  }

  @Override
  public boolean hasWork() {
    return desiredTill && tillLevel < 1f;
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    return new Vector2i[] {
      new Vector2i(x - 1, y - 1),
      new Vector2i(x,     y - 1),
      new Vector2i(x + 1, y - 1),
      new Vector2i(x - 1, y + 0),
      new Vector2i(x,     y + 0),
      new Vector2i(x + 1, y + 0),
      new Vector2i(x - 1, y + 1),
      new Vector2i(x,     y + 1),
      new Vector2i(x + 1, y + 1),
    };
  }

  @Override
  public Vector2i getLocation() {
    return new Vector2i(x, y);
  }

  @Override
  public String getJobName() {
    return "Till Soil";
  }

  @Override
  public void doWork() {
    tillLevel += 0.005f;
  }
}
