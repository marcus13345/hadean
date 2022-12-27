package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.worldobjects.FarmPlot;
import xyz.valnet.hadean.gameobjects.worldobjects.Tree;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Tile extends WorldObject implements IWorkable {

  private static int redSeed = (int)(Math.random() * 10000);
  private static int greenSeed = (int)(Math.random() * 10000);
  private static int blueSeed = (int)(Math.random() * 10000);

  // private final int x, y;
  private Vector4f color;
  private final Sprite sprite = Assets.defaultTerrain[(int)Math.floor(Math.random() * Assets.defaultTerrain.length)];

  private List<ITileThing> stuff = new ArrayList<ITileThing>();
  // TODO remove remove queue, cause like, we dont iterate over
  // things? so why remove queue them? that just leads to unneccesary
  // timing issues. you dumb fuck.
  private List<ITileThing> toRemove = new ArrayList<ITileThing>();

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Vector2i getCoords() {
    return new Vector2f(x, y).asInt();
  }

  public void start() {
    super.start();
    if(Math.random() > 0.97) {
      Tree tree = new Tree((int)x, (int)y);
      stuff.add(tree);
      add(tree);
    }

    float scale = 1;

    float red =   (float) terrain.getNoise(redSeed,   x * scale, y * scale);
    float green = (float) terrain.getNoise(greenSeed, x * scale, y * scale);
    float blue =  (float) terrain.getNoise(blueSeed,  x * scale, y * scale);

    color = new Vector4f(red * 0.1f, 0.4f + green * 0.15f, blue * 0.05f, 1f);
    // color = new Vector4f(red, green, blue, 1.0f);
  }

  public boolean isTileFree() {
    if(!isWalkable()) return false;
    for(ITileThing thing : stuff) {
      if(thing instanceof Item) return false;
    }
    return true;
  }

  public void placeThing(ITileThing thing) {
    stuff.add(thing);
    if(thing instanceof GameObject) {
      add((GameObject)thing);
    }
    thing.onPlaced(this);
    if(thing instanceof FarmPlot) {
      desiredTill = true;

      get(JobBoard.class).postSimpleWorkJob("Till Soil", this);
    }
  }

  public <T extends ITileThing> T removeThing(T thing) {
    if(!(stuff.contains(thing))) return null;
    if(toRemove.contains(thing)) return null;

    toRemove.add(thing);
    return thing;
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
  public Vector2i[] getWorkablePositions() {
    return new Vector2i[] {
      new Vector2i((int)x - 1, (int)y - 1),
      new Vector2i((int)x,     (int)y - 1),
      new Vector2i((int)x + 1, (int)y - 1),
      new Vector2i((int)x - 1, (int)y + 0),
      new Vector2i((int)x,     (int)y + 0),
      new Vector2i((int)x + 1, (int)y + 0),
      new Vector2i((int)x - 1, (int)y + 1),
      new Vector2i((int)x,     (int)y + 1),
      new Vector2i((int)x + 1, (int)y + 1),
    };
  }

  @Override
  public String getJobName() {
    return "Till Soil";
  }

  @Override
  public boolean doWork() {
    tillLevel += 0.005f;
    tillLevel = Math.min(tillLevel, 1);
    return tillLevel >= 1;
  }

  @Override
  public String getName() {
    if (tillLevel == 0) {
      return "Ground";
    } else if (tillLevel < 1) {
      return "Tilled Soil (" + Math.floor(tillLevel * 100) + "%)";
    } else return "Tilled Soil";
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x+1, y+1);
  }

  public String toThingsString() {
    if(stuff.size() == 0) return "  - Nothing";
    String str = "";
    for(ITileThing thing : stuff) {
      str += "  - " + thing + "\n";
    }
    return str.stripTrailing();
  }
}
