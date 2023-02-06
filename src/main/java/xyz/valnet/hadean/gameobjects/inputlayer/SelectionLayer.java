package xyz.valnet.hadean.gameobjects.inputlayer;

import static xyz.valnet.engine.util.Math.*;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.App;
import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Box;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.ui.ExclusivityManager;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class SelectionLayer extends GameObject implements IMouseCaptureArea, ITransient {

  public Vector2f initialCoords;
  private Camera camera;
  private float animation = 0;
  private float animationMax = 30;
  private float animationAmplitude = 0.2f;
  private List<ISelectionChangeListener> listeners = new ArrayList<ISelectionChangeListener>();

  @Override
  public void start() {
    camera = get(Camera.class);
  }

  public void subscribe(ISelectionChangeListener listener) {
    listeners.add(listener);
  }

  // TODO implement click vs single select using distance
  // private float distance(Vector2f a, Vector2f b) {
  //   return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
  // }

  private List<ISelectable> toRemove = new ArrayList<ISelectable>();

  @Override
  public void update(float dTime) {
    if(animation < animationMax) animation += dTime;
    if(animation > animationMax) animation = animationMax;

    // if(!active) return;

    // if any of our selection just RANDOMLY isnt in the scene anymore, well
    // stop selecting it dumbass
    for(ISelectable selectable : selected) {
      if(selectable instanceof GameObject) {
        if(!((GameObject)selectable).inScene()) {
          toRemove.add(selectable);
        }
      }
    }
    if(!toRemove.isEmpty()) {
      for(ISelectable removeMe : toRemove) {
        selected.remove(removeMe);
      }
      toRemove.clear();
      broadcastSelectionChanged();
    }
  }

  @Override
  public void render() {

    Drawing.setLayer(Layers.AREA_SELECT_BOX);

    float t = animation / animationMax;
    float p = lerp(animationAmplitude, 0, t);

    for(ISelectable thing : selected) {
      Box box = thing.getWorldBox().outset(p);
      camera.draw(Layers.SELECTION_IDENTIFIERS, Assets.selectedFrame, box);
      thing.selectedRender();
    }

    if(initialCoords != null) {
      camera.draw(Layers.AREA_SELECT_BOX, Assets.selectionFrame, Box.fromPoints(
        initialCoords,
        camera.getWorldMouse()
      ));
    }
  }

  private List<ISelectable> selected = new ArrayList<ISelectable>();

  private void makeSelection(Box selectionBox) {
    List<ISelectable> newSelection = new ArrayList<ISelectable>();
    List<ISelectable> selectables = getAll(ISelectable.class);

    int prio = Integer.MIN_VALUE;

    for(ISelectable thing : selectables) {
      Box thingBox = thing.getWorldBox();
      if(selectionBox.intersects(thingBox)) {
        int thingPrio = thing.getSelectPriority().toValue();
        if(thingPrio > prio) {
          newSelection.clear();
          prio = thingPrio;
        }
        if(thingPrio >= prio) {
          newSelection.add(thing);
        }
      }
    }

    animation = 0;
    updateSelection(newSelection);

  }

  private void broadcastSelectionChanged() {
    
    for(ISelectionChangeListener listener : listeners) {
      listener.selectionChanged(selected);
    }
  }

  public void updateSelection(List<ISelectable> newSelection) {
    if(selected.size() == 0 && newSelection.size() == 0) return;
    selected = newSelection;
    broadcastSelectionChanged();
  }

  public boolean rectanglesIntersect( 
    float minAx, float minAy, float maxAx, float maxAy,
    float minBx, float minBy, float maxBx, float maxBy ) {
    boolean aLeftOfB = maxAx < minBx;
    boolean aRightOfB = minAx > maxBx;
    boolean aAboveB = minAy > maxBy;
    boolean aBelowB = maxAy < minBy;

    return !( aLeftOfB || aRightOfB || aAboveB || aBelowB );
  }

  private boolean active = false;

  @Override
  public void mouseEnter() {
    active = true;
  }

  @Override
  public void mouseLeave() {
    active = false;
  }

  @Override
  public List<Box> getGuiBoxes() {
    return List.of(new Box(0, 0, 10000, 10000));
  }

  @Override
  public float getLayer() {
    return 0;
  }

  @Override
  public void mouseDown(int button) {
    if(!active) return;
    if(isPaused()) return;
    
    if(button == 0) {
      if(initialCoords == null) {
        initialCoords = camera.screen2world(new Vector2f(App.mouseX, App.mouseY));
      }
    } else if (button == 1) {
      if(selected.size() == 0) {
        get(ExclusivityManager.class).backOrDefault();
      } else {
        clearSelection();
      }
    }
  }

  public void clearSelection() {
    if(selected.size() == 0) return;
    selected = new ArrayList<ISelectable>();
    broadcastSelectionChanged();
  }

  @Override
  public void mouseUp(int button) {
    if(initialCoords != null && button == 0) {
      makeSelection(Box.fromPoints(initialCoords, camera.getWorldMouse()));
      initialCoords = null;
    }
  }
}
