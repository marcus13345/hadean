package xyz.valnet.engine.scenegraph;

import java.util.List;

import xyz.valnet.engine.math.Box;

public interface IMouseCaptureArea {
  
  public default void mouseEnter() {}
  public default void mouseLeave() {}
  public List<Box> getGuiBoxes();
  public float getLayer();

  public void mouseDown(int button);
  public void mouseUp(int button);
  public default void scrollUp() {}
  public default void scrollDown() {}

}
