package xyz.valnet.hadean.gameobjects.worldobjects.zones;

import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.hadean.gameobjects.terrain.Tile;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class FarmPlot extends Zone {

  @Override
  public void renderAlpha() {
    if(!visible) return;
    Vector4i pos = getWorldPosition();
    Assets.flat.pushColor(new Color(0.4f, 1f, 0.3f, 0.2f));
    camera.draw(Layers.TILES, Assets.whiteBox, pos.x, pos.y, pos.z, pos.w);
    Assets.flat.popColor();
  }

  private static Action TOGGLE_VISIBILITY = new Action("Hide\n----\nShow");

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
    return "Farm Plot";
  }

  @Override
  public void onPlaced(Tile tile) {}

  @Override
  public ISelectable.Priority getSelectPriority() {
    return ISelectable.Priority.LOW;
  }
}
