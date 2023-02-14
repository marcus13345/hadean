package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.ui.WorkshopOrdersUI;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.IWorkshop;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class MasonWorkshop extends Construction implements IWorkshop {

  private static Action OPEN_ORDERS = new Action("Orders");
  private transient WorkshopOrdersUI ordersWindow;

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
  }

  @Override
  public String getBuildTabName() {
    return "Mason";
  }

  @Override
  public String getBuildTabCategory() {
    return "Workshops";
  }
  
}
