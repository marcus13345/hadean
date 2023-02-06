package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Box;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.detail.Detail;

public interface ISelectable {

  public enum Priority {
    LOW(-10),
    NORMAL(0),
    HIGH(10);

    private int value;

    Priority(int value) {
      this.value = value;
    }

    public int toValue() {
      return value;
    }
  }

  public Box getWorldBox();
  public Action[] getActions();
  public void runAction(Action action);
  public Detail[] getDetails();
  public default void selectedRender() {}
  public default Priority getSelectPriority() {
    return Priority.NORMAL;
  }
  public String getName();
  public default String getGenericName() {
    return getName();
  }
}