package xyz.valnet.hadean.gameobjects.worldobjects;

import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Job;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.IItemReceiver;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.BooleanDetail;
import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.ObjectDetail;
import xyz.valnet.hadean.util.detail.PercentDetail;

@BuildableMetadata(category = "Furniture", name = "Bed", type = BuildableMetadata.SINGLE)
public class Bed extends WorldObject implements IBuildable, IItemReceiver, IWorkable, ISelectable {

  private int logs = 0;
  private float work = 0;
  private final float maxWork = 500;

  private Job job = null;

  @Override
  protected void create() {
    super.create();
    job = add(new Job("Build Bed"));
    Log log = get(Log.class);
    job.addStep(job.new PickupItemByPredicate(Log.LOG_PREDICATE));
    job.addStep(job.new DropoffPredicateAtItemReceiver(this, Log.LOG_PREDICATE));
    job.addStep(job.new Work(this));
    get(JobBoard.class).postJob(job);
  }

  @Override
  public void render() {
    super.render();
    if(isBuilt()) {
      camera.draw(Layers.GROUND, Assets.bed, (int)x, (int)y, 1, 2);
    } else {
      Assets.flat.pushColor(Vector4f.opacity(lerp(0.5f, 1.0f, work / maxWork)));
      camera.draw(Layers.GROUND, Assets.bed, (int)x, (int)y, 1, 2);
      Assets.flat.popColor();
    }
  }

  @Override
  public void buildAt(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = 1;
    this.h = 2;
  }

  @Override
  public String getName() {
    return "Bed";
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x+w, y+h);
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
    return new Vector2i[] {
      new Vector2i((int) x, (int) y - 1),

      new Vector2i((int) x - 1, (int) y),
      new Vector2i((int) x + 1, (int) y),

      new Vector2i((int) x - 1, (int) y + 1),
      new Vector2i((int) x + 1, (int) y + 1),

      new Vector2i((int) x, (int) y + 2),
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
      new PercentDetail("Work", work / maxWork),
      new ObjectDetail<Integer>("Logs", logs),
    };
  }
  
}
