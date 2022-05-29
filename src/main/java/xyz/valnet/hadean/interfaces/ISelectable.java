package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.util.Action;

public interface ISelectable {
  public Vector4f getWorldBox();
  public Action[] getActions();
  public void runAction(Action action);
  public String details();
  public default void selectedRender() {}
}