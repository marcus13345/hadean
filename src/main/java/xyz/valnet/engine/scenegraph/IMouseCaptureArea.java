package xyz.valnet.engine.scenegraph;

import xyz.valnet.engine.math.Vector4f;

public interface IMouseCaptureArea {
  public void mouseEnter();
  public void mouseLeave();
  public void mouseDown(int button);
  public void mouseUp(int button);

  public Vector4f getBox();
  public int getLayer();
}
