package xyz.valnet.engine.scenegraph;

import java.util.List;

import xyz.valnet.engine.math.Vector4f;

public interface IMouseCaptureArea {
  public void mouseEnter();
  public void mouseLeave();
  public void mouseDown(int button);
  public void mouseUp(int button);

  public List<Vector4f> getGuiBoxes();
  public float getLayer();
}
