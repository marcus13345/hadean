package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class Rice extends Item {


  public Rice(int x, int y) {
    setPosition(x, y);
  }

  @Override
  public void render() {
    Vector2i pos = getWorldPosition().xy();
    camera.draw(Layers.AIR, Assets.riceBag, pos.x, pos.y);

    Assets.flat.pushColor(Color.black);
    Vector2i screeCoords = camera.world2screen(pos.x, pos.y);
    Assets.miniFont.drawString("123", (int)screeCoords.x, (int)screeCoords.y);
    Assets.flat.popColor();
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
  public Action[] getActions() {
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {
  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {};
  }

  @Override
  public String getName() {
    return "Rice";
  }
  
}
