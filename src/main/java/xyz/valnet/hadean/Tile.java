package xyz.valnet.hadean;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.ITileThing;
import xyz.valnet.hadean.gameobjects.Tree;
import xyz.valnet.hadean.util.Assets;

// TODO make these tiles REAL gameobjects...
public class Tile extends GameObject {

  private Camera camera;

  private final int x, y;
  private final Vector4f color = new Vector4f((float) Math.random() * 0.1f, 0.4f + (float) Math.random() * 0.15f, (float) Math.random() * 0.05f, 1f);
  private final Sprite sprite = Assets.defaultTerrain[(int)Math.floor(Math.random() * Assets.defaultTerrain.length)];

  private List<ITileThing> stuff = new ArrayList<ITileThing>();

  public Tile(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void start() {
    camera = get(Camera.class);

    if(Math.random() > 0.98) {
      Tree tree = new Tree(x, y);
      stuff.add(tree);
      add(tree);
    }
  }

  @Override
  public void render() {
    Drawing.setLayer(2f);
    Assets.flat.pushColor(color);
    camera.draw(sprite, x, y);
    Assets.flat.popColor();
  }

  public boolean isWalkable() {
    for(ITileThing thing : stuff) {
      if(!thing.isWalkable()) return false;
    }
    return true;
  }
}
