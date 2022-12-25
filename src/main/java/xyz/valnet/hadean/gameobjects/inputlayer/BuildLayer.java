package xyz.valnet.hadean.gameobjects.inputlayer;

import xyz.valnet.engine.App;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.interfaces.IBuildLayerListener;
import xyz.valnet.hadean.util.Assets;
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

  @Override
  public void render() {
    if(mouseDown && active) {
      camera.screen2world(App.mouseX, App.mouseY);
    }
  }

  public void activate(IBuildLayerListener listener) {
    active = true;
    this.listener = listener;
    broadcastWorldCoords();
  }

  private void broadcastWorldCoords() {
    Vector2i worldcoords = camera.screen2worldI(App.mouseX, App.mouseY);
    if(mouseDown) {
      Vector2i[] ords = orderCoords(new Vector2i(x, y), worldcoords);
      listener.update(ords[0].x, ords[0].y, ords[2].x + 1, ords[2].y + 1);
      return;
    }
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

  private int x, y;
  private int screenX, screenY;
  private boolean mouseDown = false;

  @Override
  public void mouseDown(int button) {
    if(button == 1 && active && hovered) {
      listener.cancel();
      deactiveate();
    } else if(button == 0 && active && hovered) {
      Vector2i worldcoords = camera.screen2worldI(App.mouseX, App.mouseY);
      mouseDown = true;
      screenX = App.mouseX;
      screenY = App.mouseY;
      x = worldcoords.x;
      y = worldcoords.y;
    }
  }

  @Override
  public void mouseUp(int button) {
    if(button == 0 && active && mouseDown) {
      Vector2i worldcoords = camera.screen2worldI(App.mouseX, App.mouseY);
      mouseDown = false;
      int x1 = x;
      int y1 = y;
      int x2 = worldcoords.x;
      int y2 = worldcoords.y;
      int minX = Math.min(x1, x2);
      int minY = Math.min(y1, y2);
      int maxX = Math.max(x1, x2);
      int maxY = Math.max(y1, y2);
      listener.select(minX, minY, maxX, maxY);
    }
  }

  

  private Vector2i[] orderCoords(Vector2i a, Vector2i b) {
    return new Vector2i[] {
      new Vector2i(Math.min(a.x, b.x), Math.min(a.y, b.y)),
      new Vector2i(Math.max(a.x, b.x), Math.max(a.y, b.y)),
      new Vector2i(Math.max(a.x, b.x) - Math.min(a.x, b.x), Math.max(a.y, b.y) - Math.min(a.y, b.y))
    };
  }

  @Override
  public Vector4f getBox() {
    return active ? new Vector4f(0, 0, 1024, 576) : Vector4f.zero;
  }

  @Override
  public float getLayer() {
    return Layers.BUILD_INTERACTABLE;
  }
  
}
