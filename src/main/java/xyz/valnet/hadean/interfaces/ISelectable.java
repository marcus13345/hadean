package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.detail.Detail;

public interface ISelectable {
  public Vector4f getWorldBox();
  public Action[] getActions();
  public void runAction(Action action);
  public Detail[] getDetails();
  public default void selectedRender() {}
}