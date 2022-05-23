package xyz.valnet.hadean.input;

import xyz.valnet.engine.math.Vector4f;

public interface IMouseListener {
  public void mouseEnter();
  public void mouseLeave();
  public boolean mouseDown(int button);
  public void mouseUp(int button);

  public Vector4f getBox();
  public int getLayer();
}
