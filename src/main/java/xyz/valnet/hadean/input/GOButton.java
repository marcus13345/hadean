package xyz.valnet.hadean.input;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.util.Assets;

public class GOButton extends GameObject implements IMouseCaptureArea {

  private boolean hovered = false;
  public int layer = 1;
  public int x, y, w, h;

  public GOButton setDimensions(int x, int y, int w, int h, int l) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.layer = l;
    return this;
  }

  @Override
  public void render() {
    Drawing.setLayer(layer);
    if(hovered) {
      Assets.uiFrameLight.draw(x, y, w, h);
    } else {
      Assets.uiFrame.draw(x, y, w, h);
    }
  }

  @Override
  public void mouseEnter() {
    hovered = true;
  }

  @Override
  public void mouseLeave() {
    hovered = false;
  }

  @Override
  public void mouseDown(int button) {
    
  }

  @Override
  public void mouseUp(int button) {
    
  }

  @Override
  public Vector4f getBox() {
    return new Vector4f(x, y, w, h);
  }

  @Override
  public float getLayer() {
    return layer;
  }
  
}
