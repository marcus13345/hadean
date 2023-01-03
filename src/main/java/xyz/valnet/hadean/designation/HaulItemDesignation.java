package xyz.valnet.hadean.designation;

import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.BuildableMetadata;

@BuildableMetadata(category = "Jobs", name = "Haul Items")
public class HaulItemDesignation extends Designation<Item> {
  @Override
  protected Class<Item> getType() {
    return Item.class;
  }

  @Override
  protected void designate(Item thing) {
    thing.runAction(Item.HAUL);
  }
}
