package xyz.valnet.hadean.gameobjects.jobs;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IWorkshop;

public class WorkOrderManager extends GameObject {

  private Set<WorkOrder> orders;

  @Override
  protected void start() {
    super.start();
    this.onAddGameObject((obj) -> {
      if(obj instanceof WorkOrder) {
        orders.add((WorkOrder) obj);
      }
    });
    this.onRemoveGameObject((obj) -> {
      if(obj instanceof WorkOrder) {
        orders.remove((WorkOrder) obj);
      }
    });
  }

  @Override
  protected void create() {
    super.create();
    orders = new HashSet<>();
  }

  public Set<WorkOrder> getOrders(IWorkshop shop) {
    return orders.stream()
      .filter((order) -> order.validForShop(shop))
      .collect(Collectors.toSet());
  }
  
}
