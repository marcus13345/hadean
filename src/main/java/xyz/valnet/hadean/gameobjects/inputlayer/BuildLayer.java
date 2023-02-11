package xyz.valnet.hadean.gameobjects.inputlayer;

import java.util.List;

import xyz.valnet.engine.App;
import xyz.valnet.engine.math.Box;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.interfaces.BuildType;
import xyz.valnet.hadean.interfaces.IBuildLayerListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class BuildLayer extends GameObject implements IMouseCaptureArea, ITransient {

  private boolean hovered = false;

  private Camera camera;

  private boolean active = false;

  private IBuildLayerListener listener = null;

  private BuildType type = BuildType.AREA;

  public void setBuildType(BuildType type) {
    this.type = type;
  }

  @Override
  protected void connect() {
    camera = get(Camera.class);
  }

  @Override
  public void update(float dTime) {
    if(listener == null) return;
    if(type == BuildType.SINGLE && mouseDown) return;
    
    broadcastWorldCoords();
  }

  @Override
  public void render() {
    if(mouseDown && active) {
      camera.screen2world(App.mouseX, App.mouseY);
    }
  }

  public void activate(IBuildLayerListener listener) {
    if(active) return;
    active = true;
    this.listener = listener;
    broadcastWorldCoords();
  }

  private void broadcastWorldCoords() {
    Vector2i worldcoords = camera.getWorldMouse().asInt();
    if(mouseDown) {
      listener.update(Box.fromPoints(startingPoint, camera.getWorldMouse()));
      return;
    }
    if(type == BuildType.SINGLE && dimensions != null) {
      listener.update(new Box(worldcoords, dimensions));
    } else {
      listener.update(new Box(worldcoords, 1, 1));
    }
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

  private Vector2i startingPoint = null;
  private boolean mouseDown = false;

  private Vector2i dimensions = new Vector2i(1, 1);

  public void setDimensions(Vector2i dimensions) {
    this.dimensions = dimensions;
  }

  @Override
  public void mouseDown(int button) {
    if(button == 1 && active && hovered) {
      Assets.sndCancel.play();
      listener.cancel();
    } else if(button == 0 && active && hovered) {
      // TODO this conversion in negative numbers definitely works wrong.
      startingPoint = camera.getWorldMouse().asInt();
      mouseDown = true;
      if(type == BuildType.SINGLE) {
        listener.build(new Box(startingPoint, dimensions));
      }
    }
  }

  @Override
  public void mouseUp(int button) {
    if(button == 0 && active && mouseDown) {
      mouseDown = false;
      if(type == BuildType.AREA) {
        listener.build(Box.fromPoints(camera.getWorldMouse(), startingPoint));
      }
    }
  }

  @Override
  public List<Box> getGuiBoxes() {
    return List.of(active ? new Box(0, 0, 1024, 576) : Box.none);
  }

  @Override
  public float getLayer() {
    return Layers.BUILD_INTERACTABLE;
  }
  
}
