package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

@BuildableMetadata(category = "Zones", name = "Farm Plot")
public class FarmPlot extends WorldObject implements ISelectable, ITileThing, IBuildable {

  private int w, h;

  @Override
  public void renderAlpha() {
    if(!visible) return;
    Assets.flat.pushColor(new Vector4f(0.4f, 1f, 0.3f, 0.2f));
    camera.draw(Layers.GROUND, Assets.whiteBox, x, y, w, h);
    Assets.flat.popColor();
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
  public String details() {
    return "";
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
    System.out.println("buildAt");
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    System.out.println("<" + x + ", " + y + ", " + w + ", " + h + ">");
    System.out.println(inScene());
    
    for(int i = x; i < x + w; i ++) {
      for(int j = y; j < y + h; j ++) {
        terrain.getTile(i, j).placeThing(this);
      }
    }
  }

  @Override
  public String getName() {
    return "Farm Plot";
  }

}
