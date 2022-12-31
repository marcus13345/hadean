package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class Rice extends Item implements ITileThing, ISelectable {

  // private SmartBoolean haul;

  private JobBoard jobboard;

  @Override
  public void start() {
    super.start();
    jobboard = get(JobBoard.class);
    Rice that = this;

    // haul = new SmartBoolean(false, new IListener() {
    //   @Override
    //   public void rise() {
    //     jobboard.postJob(that);
    //   }

    //   @Override
    //   public void fall() {
    //     jobboard.rescindJob(that);
    //   }
    // });
  }

  public Rice(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render() {
    camera.draw(Layers.AIR, Assets.riceBag, x, y);

    Assets.flat.pushColor(Vector4f.black);
    Vector2f screeCoords = camera.world2screen(x, y);
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
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
  }

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
