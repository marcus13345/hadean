package xyz.valnet.hadean.util;

public class SmartBoolean {
  private boolean value;

  public interface IListener {
    public default void rise() {}
    public default void fall() {}
    public default void changed() {}
  }

  private IListener isbl;

  public SmartBoolean(boolean v, IListener isbl) {
    value = v;
    this.isbl = isbl;
  }

  public void set(boolean b) {
    if(value != b) {
      value = b;
      isbl.changed();
      if(b) {
        isbl.rise();
      } else {
        isbl.fall();
      }
    }
  }

  public void toggle() {
    set(!value);
  }

  public boolean value() {
    return value;
  }

}
