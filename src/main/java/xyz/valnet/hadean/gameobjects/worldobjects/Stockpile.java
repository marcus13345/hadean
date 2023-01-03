package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

@BuildableMetadata(category = "Zones", name = "Stockpile")
public class Stockpile extends WorldObject implements ISelectable, ITileThing, IBuildable {

  private int w, h;

  @Override
  public void render() {
  }

  @Override
  public void renderAlpha() {
    if(!visible) return;
    Assets.flat.pushColor(new Vector4f(1f, 0.2f, 0.1f, 0.3f));
    camera.draw(Layers.TILES, Assets.whiteBox, x, y, w, h);
    Assets.flat.popColor();
  }

  @Override
  public void update(float dTime) {
    super.update(dTime);

  }

  private Tile[] getTiles() {
    Vector4f box = getWorldBox();
    int count = 0;
    Tile[] tiles = new Tile[(int)box.z * (int)box.w];
    for(float x = box.x; x < box.z; x ++) {
      for(float y = box.y; y < box.w; y ++) {
        tiles[count] = terrain.getTile((int)x, (int)y);
        count ++;
      }
    }
    return tiles;
  }

  public Vector2i getFreeTile() {
    Tile[] tiles = getTiles();
    for(Tile tile : tiles) {
      if(tile.isTileFree()) return tile.getCoords();
    }
    return null;
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + w, y + h);
  }

  private static Action TOGGLE_VISIBILITY = new Action("Hide / Show");

  @Override
  public Action[] getActions() {
    return new Action[] { TOGGLE_VISIBILITY };
  }

  private boolean visible = true;

  @Override
  public void runAction(Action action) {
    if(action == TOGGLE_VISIBILITY) {
      visible = !visible;
    }
  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {};
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
  public void onRemove() {
    
  }

  @Override
  public void buildAt(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    
    for(int i = x; i < x + w; i ++) {
      for(int j = y; j < y + h; j ++) {
        terrain.getTile(i, j).placeThing(this);
      }
    }
  }

  @Override
  public String getName() {
    return "Stockpile";
  }

  @Override
  public void onPlaced(Tile tile) {}

}
