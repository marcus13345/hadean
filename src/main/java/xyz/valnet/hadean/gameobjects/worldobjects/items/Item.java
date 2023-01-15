package xyz.valnet.hadean.gameobjects.worldobjects.items;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.hadean.gameobjects.Job;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public abstract class Item extends WorldObject implements ISelectable, ITileThing {
  protected JobBoard jobboard;

  private Job haulJob = null;

  @Override
  protected void connect() {
    super.connect();
    jobboard = get(JobBoard.class);
  }
  
  protected void create() {
    super.create();
    if(haulOnCreate()) markForHaul();
  }

  protected boolean haulOnCreate() {
    return true;
  }

  @Override
  public String getName() {
    return null;
  }

  public static final Action HAUL = new Action("Haul");
  public static final Action CANCEL_HAUL = new Action("Cancel\n Haul");

  @Override
  public Action[] getActions() {
    Action[] actions = new Action[1];
    if(haulJob == null) actions[0] = HAUL;
    else actions[0] = CANCEL_HAUL;
    return actions;
  }

  @Override
  public void renderAlpha() {
    if(haulJob != null) {
      // Assets.flat.pushColor(Vector4f.opacity(1f));
      camera.draw(Layers.MARKERS, Assets.haulArrow, getWorldPosition().xy().asFloat());
      // Assets.flat.popColor();
    }
  }

  @Override
  public void runAction(Action action) {
    if (action == HAUL) {
      markForHaul();
    } else if (action == CANCEL_HAUL) {
      cancelHaul();
    }
  }

  private void cancelHaul() {
    if(haulJob == null) return;
    haulJob.close();
    haulJob = null;
  }

  private void markForHaul() {
    if(haulJob != null) return;
    haulJob = add(new Job("Haul " + this.getName()));
    haulJob.addStep(haulJob.new PickupItem(this));
    haulJob.addStep(haulJob.new DropoffAtStockpile(this));
    haulJob.registerClosedListener(() -> {
      haulJob = null;
    });
    jobboard.postJob(haulJob);
  }

  @Override
  public void onPlaced(Tile tile) {
    setPosition(tile.getWorldPosition());
  }

  public boolean matches(IItemPredicate itemPredicate) {
    return itemPredicate.matches(this);
  }



  public void setPosition(Vector4i vector) {
    super.setPosition(vector);
  }

  public void setPosition(Vector2i vector) {
    super.setPosition(vector);
  }

  public void setPosition(int x, int y) {
    super.setPosition(x, y);
  }

  public void setPosition(int x, int y, int w, int h) {
    super.setPosition(x, y, w, h);
  }
}
