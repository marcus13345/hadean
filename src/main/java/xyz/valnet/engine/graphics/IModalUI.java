package xyz.valnet.engine.graphics;

// TODO there is some shared logic for these in tabs & selection ui.
// combine them into a base class, probably extending immediateUI...

public interface IModalUI {
  public void open();
  public void close();
  public default void back() {
    close();
  }
}
