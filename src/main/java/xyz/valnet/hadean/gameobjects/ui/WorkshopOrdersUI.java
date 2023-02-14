package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.graphics.LayoutStyle;
import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.gameobjects.jobs.WorkOrder;
import xyz.valnet.hadean.gameobjects.jobs.WorkOrderManager;
import xyz.valnet.hadean.interfaces.IWorkshop;

public class WorkshopOrdersUI extends ImmediateUI implements ITransient {

  private IWorkshop shop;
  private SelectionLayer selectionLayer;
  private WorkOrderManager manager;

  public void open(IWorkshop shop) {
    this.shop = shop;
  }

  public void close() {
    this.shop = null;
  }
  
  @Override
  protected void gui() {
    if(shop == null) return;
    
    window(0, 0, 200, 300, () -> {
      var all = manager.getOrders(shop);
      var specifics = all.stream()
        .filter(workorder -> workorder.isSpecificToShop(shop))
        .toList();
      
      for(WorkOrder order : specifics) {
        group(() -> {
          text(order.getName());
          space(4);
          horizontal(() -> {
            if(button("--", LayoutStyle.sized(32, 16))) {
              order.decreaseCount();
            }
            space(4);
            if(button("-", LayoutStyle.sized(16, 16))) {
              order.decreaseCount();
            }
            space(4);
            vertical(() -> {
              // space(8);
              text("" + order.getCount());
            });
            space(4);
            if(button("+", LayoutStyle.sized(16, 16))) {
              order.increaseCount();
            }
            space(4);
            if(button("++", LayoutStyle.sized(32, 16))) {
              order.increaseCount();
            }
          });
        });
        space(8);
      }

      text("" + (all.size() - specifics.size()) + " implicit general orders");
      space(8);

      if(button("New Order")) {
        add(new WorkOrder().setShop(shop));
      }
    });
  }

  @Override
  protected void connect() {
    super.connect();
    selectionLayer = get(SelectionLayer.class);
    manager = get(WorkOrderManager.class);
  }

  @Override
  protected void start() {
    super.start();
    this.selectionLayer.subscribe((selection) -> {
      if(shop == null) {
        return;
      } else if(selection.size() != 1) {
        close();
      } else if(selection.get(0) instanceof IWorkshop) {
        shop = (IWorkshop) selection.get(0);
      }
    });
  }
  
}
