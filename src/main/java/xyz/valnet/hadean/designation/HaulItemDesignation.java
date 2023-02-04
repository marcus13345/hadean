package xyz.valnet.hadean.designation;

import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;

public class HaulItemDesignation extends Designation<Item> {

  @Override
  protected Class<Item> getType() {
    return Item.class;
  }

  @Override
  protected void designate(Item thing) {
    thing.runAction(Item.HAUL);
  }

  @Override
  public String getBuildTabName() {
    return "Haul Items";
  }
}
