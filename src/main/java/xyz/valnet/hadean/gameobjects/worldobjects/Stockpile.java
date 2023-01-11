package xyz.valnet.hadean.gameobjects.worldobjects;

import java.util.Set;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

@BuildableMetadata(category = "Zones", name = "Stockpile")
public class Stockpile extends Buildable {

  @Override
  public void render() {
  }

  @Override
  public void renderAlpha() {
    if(!visible) return;
    Vector4i pos = getWorldPosition();
    Assets.flat.pushColor(new Vector4f(1f, 0.2f, 0.1f, 0.3f));
    camera.draw(Layers.TILES, Assets.whiteBox, pos.x, pos.y, pos.z, pos.w);
    Assets.flat.popColor();
  }

  public Vector2i getFreeTile() {
    Set<Tile> tiles = getTiles();
    for(Tile tile : tiles) {
      if(tile.isTileFree()) return tile.getCoords();
    }
    return null;
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
  public String getName() {
    return "Stockpile";
  }

  @Override
  public void onPlaced(Tile tile) {}

  @Override
  public ISelectable.Priority getSelectPriority() {
    return ISelectable.Priority.LOW;
  }
}
