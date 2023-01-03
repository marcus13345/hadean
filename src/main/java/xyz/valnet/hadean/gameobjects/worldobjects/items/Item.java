package xyz.valnet.hadean.gameobjects.worldobjects.items;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Job;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
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

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
  }

  public static final Action ACTION_HAUL = new Action("Haul");

  @Override
  public Action[] getActions() {
    return new Action[] {
      ACTION_HAUL
    };
  }

  @Override
  public void renderAlpha() {
    if(haulJob != null) {
      Assets.flat.pushColor(Vector4f.opacity(0.4f));
      camera.draw(Layers.GENERAL_UI, Assets.haulArrow, getWorldPosition());
      Assets.flat.popColor();
    }
  }

  @Override
  public void runAction(Action action) {
    if (action == ACTION_HAUL) {
      toggleHaul();
    }
  }

  private void toggleHaul() {
    if(haulJob == null) {
      markForHaul();
    } else {
      cancelHaul();
    }
  }

  private void cancelHaul() {
    if(haulJob == null) return;
    jobboard.rescindJob(haulJob);
    haulJob = null;
  }

  private void markForHaul() {
    if(haulJob != null) return;
    haulJob = add(new Job("Haul " + this.getName()));
    haulJob.addStep(haulJob.new PickupItem(this, new Vector2f[] { this.getWorldPosition() }));
    haulJob.addStep(haulJob.new DropoffAtStockpile(this));
    haulJob.registerClosedListener(() -> {
      haulJob = null;
    });
    jobboard.postJob(haulJob);
  }

  @Override
  public void onPlaced(Tile tile) {
    this.x = tile.getCoords().x;
    this.y = tile.getCoords().y;
  }
}
