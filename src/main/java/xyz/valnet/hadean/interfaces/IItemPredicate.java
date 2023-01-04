package xyz.valnet.hadean.interfaces;

import java.io.Serializable;

import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;

@FunctionalInterface
public interface IItemPredicate extends Serializable {
  public boolean matches(Item item);
}
