package xyz.valnet.hadean.gameobjects.jobs;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IWorkshop;

public class WorkOrder extends GameObject {
  
  private boolean recurring = false;
  private int count = 10;
  private String name = "Cut Stone Blocks";
  private IWorkshop shop = null;

  public boolean getRecurring() {
    return recurring;
  }

  public int getCount() {
    return count;
  }

  public String getName() {
    return name;
  }

  public void increaseCount() {
    count ++;
  }

  public void decreaseCount() {
    count --;
  }

  public WorkOrder setShop(IWorkshop shop) {
    this.shop = shop;
    return this;
  }

  public boolean validForShop(IWorkshop shop) {
    return this.shop == null || this.shop == shop;
  }

  public boolean isSpecificToShop(IWorkshop shop) {
    return this.shop != null && shop == this.shop;
  }
}
