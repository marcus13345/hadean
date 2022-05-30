package xyz.valnet.hadean.gameobjects.inputlayer;

import xyz.valnet.engine.App;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.interfaces.IBuildLayerListener;
import xyz.valnet.hadean.util.Layers;

public class BuildLayer extends GameObject implements IMouseCaptureArea {

  private boolean hovered = false;

  private Camera camera;

  private boolean active = false;

  private IBuildLayerListener listener = null;

  @Override
  public void start() {
    camera = get(Camera.class);
  }

  @Override
  public void update(float dTime) {
    if(listener == null) return;
    broadcastWorldCoords();
  }

  public void activate(IBuildLayerListener listener) {
    active = true;
    this.listener = listener;
    broadcastWorldCoords();
  }

  private void broadcastWorldCoords() {
    Vector2f worldcoords = camera.screen2world(App.mouseX, App.mouseY);
    listener.update(worldcoords.x, worldcoords.y, 1, 1);
  }

  public void deactiveate() {
    active = false;
    this.listener = null;
  }

  @Override
  public void mouseEnter() {
    hovered = true;
  }

  @Override
  public void mouseLeave() {
    hovered = false;
  }

  private float x, y;
  private boolean mouseDown = false;

  @Override
  public void mouseDown(int button) {
    if(button == 1 && active && hovered) {
      listener.cancel();
      deactiveate();
    } else if(button == 0 && active && hovered) {
      Vector2f worldcoords = camera.screen2world(App.mouseX, App.mouseY);
      mouseDown = true;
      x = worldcoords.x;
      y = worldcoords.y;
    }
  }

  @Override
  public void mouseUp(int button) {
    if(button == 0 && active && mouseDown) {
      Vector2f worldcoords = camera.screen2world(App.mouseX, App.mouseY);
      mouseDown = false;
      float x1 = x;
      float y1 = y;
      float x2 = worldcoords.x;
      float y2 = worldcoords.y;
      float minX = Math.min(x1, x2);
      float minY = Math.min(y1, y2);
      float maxX = Math.max(x1, x2);
      float maxY = Math.max(y1, y2);
      listener.select(minX, minY, maxX, maxY);
    }
  }

  @Override
  public Vector4f getBox() {
    return active ? new Vector4f(0, 0, 1024, 576) : Vector4f.zero;
  }

  @Override
  public int getLayer() {
    return Layers.BUILD_INTERACTABLE;
  }
  
}
