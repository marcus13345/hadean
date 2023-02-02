package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.jobs.Job;
import xyz.valnet.hadean.gameobjects.jobs.JobBoard;
import xyz.valnet.hadean.gameobjects.worldobjects.Buildable;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IItemReceiver;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.BooleanDetail;
import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.ObjectDetail;
import xyz.valnet.hadean.util.detail.PercentDetail;

@BuildableMetadata(category = "Furniture", name = "Bed", type = BuildableMetadata.Type.SINGLE)
public class Bed extends Buildable implements IItemReceiver, IWorkable {

  private int logs = 0;
  private float work = 0;
  private final float maxWork = 500;

  private Job job = null;

  @Override
  protected Vector2i getDimensions() {
    return new Vector2i(1, 2);
  }

  @Override
  protected void create() {
    super.create();
    job = add(new Job("Build Bed"));
    job.addStep(job.new PickupItemByPredicate(Log.LOG_PREDICATE));
    job.addStep(job.new DropoffPredicateAtItemReceiver(this, Log.LOG_PREDICATE));
    job.addStep(job.new Work(this));
    get(JobBoard.class).postJob(job);
  }

  @Override
  public void render() {
    super.render();
    Vector2i pos = getWorldPosition().xy();

    if(isBuilt()) {
      camera.draw(Layers.GROUND, Assets.bed, pos.x, pos.y, 1, 2);
    } else {
      float p = work / maxWork;
      float b = 4;

      Assets.flat.pushColor(Color.grey(b).withAlpha(0.5f));
      camera.draw(Layers.GROUND, Assets.bed, pos.x, pos.y, 1, 2);
      Assets.flat.popColor();

      if(logs > 0) {
        camera.drawProgressBar(p, getWorldBox());
      }
      // Assets.uiFrame.draw(box.x -3, box.y - 6, (int)Math.round(lerp(0, box.z + 6, p)), 4);

    }
  }

  @Override
  public String getName() {
    return "Bed";
  }

  @Override
  public boolean receive(Item item) {
    if(item == null) return false;
    if(!item.matches(Log.LOG_PREDICATE)) return false;
    remove(item);
    logs ++;
    return true;
  }

  private boolean isBuilt() {
    return work >= maxWork;
  }

  @Override
  public boolean doWork(float dTime) {
    work += dTime;
    return isBuilt();
  }

  private Vector2i[] getBorders() {
    Vector2i pos = getWorldPosition().xy();
    return new Vector2i[] {
      new Vector2i(pos.x, pos.y - 1),

      new Vector2i(pos.x - 1, pos.y),
      new Vector2i(pos.x + 1, pos.y),

      new Vector2i(pos.x - 1, pos.y + 1),
      new Vector2i(pos.x + 1, pos.y + 1),

      new Vector2i(pos.x, pos.y + 2),
    };
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    return getBorders();
  }

  @Override
  public String getJobName() {
    return "Build Bed";
  }

  @Override
  public Vector2i[] getItemDropoffLocations() {
    return getBorders();
  }

  @Override
  public Action[] getActions() {
    return new Action[0];
  }

  @Override
  public void runAction(Action action) {

  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {
      new BooleanDetail("Built", isBuilt()),
      new PercentDetail("Work", work / maxWork, 1),
      new ObjectDetail<Integer>("Logs", logs),
    };
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {
    
  }
}
