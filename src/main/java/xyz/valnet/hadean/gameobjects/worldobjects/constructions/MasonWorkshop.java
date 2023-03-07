package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.TileBox;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.jobs.Job;
import xyz.valnet.hadean.gameobjects.jobs.WorkOrder;
import xyz.valnet.hadean.gameobjects.jobs.WorkOrderManager;
import xyz.valnet.hadean.gameobjects.ui.WorkshopOrdersUI;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.IWorkshop;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class MasonWorkshop extends Construction implements IWorkshop {

  private static Action OPEN_ORDERS = new Action("Orders");
  private transient WorkshopOrdersUI ordersWindow;
  private WorkOrderManager manager;

  // TODO the job itself should have the work amount...
  private float workDone = 0;

  @Override
  protected IItemPredicate getBuildingMaterial() {
    return Log.LOG_PREDICATE;
  }

  @Override
  protected int getBuildingMaterialCount() {
    return 1;
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public String getName() {
    return "Mason's Workshop";
  }

  @Override
  protected Sprite getDefaultSprite() {
    return Assets.testTile;
  }

  @Override
  public Vector2i getDimensions() {
    return new Vector2i(3, 3);
  }

  @Override
  public Action[] getActions() {
    if(isBuilt()) {
      return new Action[] { OPEN_ORDERS };
    } else return new Action[0];
  }

  @Override
  public void runAction(Action action) {
    if(action == OPEN_ORDERS) {
      ordersWindow.open(this);
    }
  }

  @Override
  protected void connect() {
    super.connect();
    ordersWindow = get(WorkshopOrdersUI.class);
    manager = get(WorkOrderManager.class);
  }

  @Override
  public String getBuildTabName() {
    return "Mason";
  }

  @Override
  public String getBuildTabCategory() {
    return "Workshops";
  }

  @Override
  public boolean doWork(float dTime) {
    workDone += dTime;
    return workDone > order.getMaxWork();
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    return getWorldBox().getBorders();
  }

  @Override
  public String getJobName() {
    return "Do Work as Workshop";
  }

  @Override
  public TileBox getSpawnableArea() {
    return getWorldBox().asTileBox();
  }

  private Job job = null;
  private WorkOrder order = null;

  @Override
  public void update(float dTime) {
    super.update(dTime);
    if(job == null) {
      tryGetJob();
    }
  }

  private final void tryGetJob() {
    for(WorkOrder order : manager.getOrders(this)) {
      if(order.canCreateJob()) {
        this.order = order;
        job = order.createNextJob(this);
        job.registerClosedListener(() -> {
          workDone = 0;
          job = null;
          this.order = null;
        });
        return;
      }
    }
  }
  
}
