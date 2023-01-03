package xyz.valnet.hadean.interfaces;

import java.io.Serializable;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;

public interface IItemReceiver extends Serializable {
  public boolean receive(Item item);
  public Vector2i[] getItemDropoffLocations();
}
