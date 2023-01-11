package xyz.valnet.engine.graphics;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import xyz.valnet.engine.math.Vector4i;

public class Tile16 {
  public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
  }

  private Sprite solo;

  private Sprite n; // north
  private Sprite e; // east
  private Sprite s; // south
  private Sprite w; // west

  private Sprite h; // horizontal
  private Sprite v; // vertical

  private Sprite cne; // corner north east
  private Sprite cnw; // corner north west
  private Sprite cse; // corner south east
  private Sprite csw; // corner south west

  private Sprite tn; // T north east west
  private Sprite te; // T north south east
  private Sprite ts; // T south east west
  private Sprite tw; // T north south west

  private Sprite all; // all sides

  private Vector4i area; // the original area in the texture

  private Map<EnumSet<Direction>, Sprite> cache = new HashMap<EnumSet<Direction>, Sprite>();

  public Tile16(Texture base, Vector4i area) {
    this.area = area;
    solo = new Sprite(base, getPixelCoords(0, 0));

    n = new Sprite(base, getPixelCoords(0, 3));
    e = new Sprite(base, getPixelCoords(3, 0));
    s = new Sprite(base, getPixelCoords(0, 1));
    w = new Sprite(base, getPixelCoords(1, 0));

    h = new Sprite(base, getPixelCoords(0, 2));
    v = new Sprite(base, getPixelCoords(2, 0));

    cne = new Sprite(base, getPixelCoords(3, 3));
    cnw = new Sprite(base, getPixelCoords(1, 3));
    cse = new Sprite(base, getPixelCoords(3, 1));
    csw = new Sprite(base, getPixelCoords(1, 1));

    tn = new Sprite(base, getPixelCoords(2, 3));
    te = new Sprite(base, getPixelCoords(3, 2));
    ts = new Sprite(base, getPixelCoords(2, 1));
    tw = new Sprite(base, getPixelCoords(1, 2));

    all = new Sprite(base, getPixelCoords(2, 2));

    cache.put(EnumSet.noneOf(Direction.class), solo);

    cache.put(EnumSet.of(Direction.NORTH), n);
    cache.put(EnumSet.of(Direction.EAST), e);
    cache.put(EnumSet.of(Direction.SOUTH), s);
    cache.put(EnumSet.of(Direction.WEST), w);

    cache.put(EnumSet.of(Direction.NORTH, Direction.SOUTH), h);
    cache.put(EnumSet.of(Direction.EAST, Direction.WEST), v);

    cache.put(EnumSet.of(Direction.NORTH, Direction.EAST), cne);
    cache.put(EnumSet.of(Direction.NORTH, Direction.WEST), cnw);
    cache.put(EnumSet.of(Direction.SOUTH, Direction.EAST), cse);
    cache.put(EnumSet.of(Direction.SOUTH, Direction.WEST), csw);

    cache.put(EnumSet.of(Direction.NORTH, Direction.EAST, Direction.WEST), tn);
    cache.put(EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.EAST), te);
    cache.put(EnumSet.of(Direction.SOUTH, Direction.EAST, Direction.WEST), ts);
    cache.put(EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.WEST), tw);

    cache.put(EnumSet.allOf(Direction.class), all);
  }

  private Vector4i getPixelCoords(int x, int y) {
    int tileWidth = area.z / 4;
    int tileHeight = area.w / 4;
    return new Vector4i(
      area.x + x * tileWidth,
      area.y + y * tileHeight,
      tileWidth,
      tileHeight
    );
  }

  public Sprite getTextureFor(EnumSet<Direction> directions) {
    return cache.get(directions);
  }
}
