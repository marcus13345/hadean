package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.worldobjects.FarmPlot;
import xyz.valnet.hadean.gameobjects.worldobjects.Tree;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Boulder;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.IPingable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Tile extends WorldObject implements IWorkable {

  private static int redSeed = (int)(Math.random() * 10000);
  private static int greenSeed = (int)(Math.random() * 10000);
  private static int blueSeed = (int)(Math.random() * 10000);

  private Color color;
  private final int tileSelector = (int)Math.floor(Math.random() * 4);
  private boolean rocks = false;

  private Set<ITileThing> stuff = new HashSet<ITileThing>();
  // TODO remove remove queue, cause like, we dont iterate over
  // things? so why remove queue them? that just leads to unneccesary
  // timing issues. you dumb fuck.
  private List<ITileThing> toRemove = new ArrayList<ITileThing>();

  public Tile(int x, int y) {
    setPosition(x, y);
  }

  public Vector2i getCoords() {
    return getWorldPosition().xy();
  }

  public void start() {
    super.start();
    Vector4i pos = getWorldPosition();

    float scale = 1;

    float red =   (float) terrain.getNoise(redSeed,   pos.x * scale, pos.y * scale);
    float green = (float) terrain.getNoise(greenSeed, pos.x * scale, pos.y * scale);
    float blue =  (float) terrain.getNoise(blueSeed,  pos.x * scale, pos.y * scale);

    if(color == null) color = new Color(red * 0.1f, 0.4f + green * 0.15f, blue * 0.05f, 1f);
  }

  @Override
  protected void create() {
    Vector4i pos = getWorldPosition();
    if(Math.random() > 0.95) {
      rocks = true;
    }
    if(Math.random() > 0.97) {
      add(new Tree(pos.x, pos.y));
    } else if(Math.random() > 0.98) {
      rocks = false;
      add(new Boulder(pos.x, pos.y));
    }
  }

  public boolean isTileFree() {
    if(!isWalkable()) return false;
    for(ITileThing thing : stuff) {
      if(thing instanceof Item) return false;
    }
    return true;
  }

  public Item pickupByItemPredicate(IItemPredicate itemPredicate) {
    for(ITileThing thing : stuff) {
      if(thing instanceof Item) {
        Item item = (Item) thing;
        if(item.matches(itemPredicate)) {
          return removeThing(item);
        }
      }
    }
    return null;
  }

  public void pingThings() {
    for(ITileThing thing : stuff) {
      if(thing instanceof IPingable) {
        ((IPingable)thing).ping();
      }
    }
  }

  private void pingNeighbors() {
    Vector2i pos = getCoords();
    Tile north = terrain.getTile(pos.x, pos.y - 1);
    Tile east = terrain.getTile(pos.x - 1, pos.y);
    Tile south = terrain.getTile(pos.x, pos.y + 1);
    Tile west = terrain.getTile(pos.x + 1, pos.y);
    if(north != null) north.pingThings();
    if(east != null) east.pingThings();
    if(south != null) south.pingThings();
    if(west != null) west.pingThings();
  }

  public void placeThing(ITileThing thing) {
    stuff.add(thing);
    if(thing instanceof GameObject) {
      add((GameObject)thing);
    }
    thing.onPlaced(this);

    pingThings();
    pingNeighbors();

    if(thing instanceof FarmPlot) {
      get(JobBoard.class).postSimpleWorkJob("Till Soil", this);
    }
  }

  public <T extends ITileThing> T removeThing(T thing) {
    if(!(stuff.contains(thing))) return null;
    if(toRemove.contains(thing)) return null;

    toRemove.add(thing);
    
    pingThings();
    pingNeighbors();

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
    Vector4i pos = getWorldPosition();
    if(tillLevel < 1f) {
      Assets.flat.pushColor(color);
      camera.draw(Layers.TILES, Assets.defaultTerrain[tileSelector], pos.x, pos.y);
      Assets.flat.popColor();
      if(rocks) camera.draw(Layers.TILES, Assets.rocks, pos.x, pos.y);
    }
    if(tillLevel > 0f) {
      Assets.flat.pushColor(Color.white.withAlpha(tillLevel));
      camera.draw(Layers.TILES, Assets.farmPlot[tileSelector], pos.x, pos.y);
      Assets.flat.popColor();
    }
  }

  public boolean isWalkable() {
    for(ITileThing thing : stuff) {
      if(!thing.isWalkable()) return false;
    }
    return true;
  }

  private float tillLevel = 0;

  @Override
  public Vector2i[] getWorkablePositions() {
    Vector4i pos = getWorldPosition();
    return new Vector2i[] {
      new Vector2i(pos.x - 1, pos.y - 1),
      new Vector2i(pos.x,     pos.y - 1),
      new Vector2i(pos.x + 1, pos.y - 1),
      new Vector2i(pos.x - 1, pos.y + 0),
      new Vector2i(pos.x,     pos.y + 0),
      new Vector2i(pos.x + 1, pos.y + 0),
      new Vector2i(pos.x - 1, pos.y + 1),
      new Vector2i(pos.x,     pos.y + 1),
      new Vector2i(pos.x + 1, pos.y + 1),
    };
  }

  @Override
  public String getJobName() {
    return "Till Soil";
  }

  @Override
  public boolean doWork(float dTime) {
    tillLevel += 0.005f * dTime;
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

  public String toThingsString() {
    if(stuff.size() == 0) return "  - Nothing";
    String str = "";
    for(ITileThing thing : stuff) {
      str += "  - " + thing + "\n";
    }
    return str.stripTrailing();
  }

  public <T> boolean has(Class<T> clazz) {
    for(ITileThing thing : stuff) {
      if(clazz.isInstance(thing)) {
        return true;
      }
    }
    return false;
  }
}
