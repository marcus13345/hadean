package xyz.valnet.hadean.gameobjects.inputlayer;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.App;
import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

import static xyz.valnet.engine.util.Math.lerp;

public class Selection extends GameObject implements IMouseCaptureArea {

  public Vector2f initialCoords;
  private Camera camera;
  private float animation = 0;
  private float animationMax = 15;
  private float animationAmplitude = 0.3f;
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
    if(animation < animationMax) animation ++;
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
      Vector4f box = thing.getWorldBox();
      Vector2f min = camera.world2screen(box.x - p, box.y - p);
      Vector2f max = camera.world2screen(box.z + p, box.w + p);
      Drawing.setLayer(Layers.SELECTION_IDENTIFIERS);
      Assets.selectedFrame.draw((int)min.x, (int)min.y, (int)(max.x - min.x), (int)(max.y - min.y));
      thing.selectedRender();
    }

    if(initialCoords != null) {
      Assets.selectionFrame.draw((int) initialCoords.x, (int) initialCoords.y, (int) (App.mouseX - initialCoords.x), (int) (App.mouseY - initialCoords.y));
    }
  }

  // this will take any x1, y1, x2, y2 vector and make x1 < x2 and y1 < y2;
  private Vector4f sortVector(Vector4f vector) {
    return new Vector4f(
      Math.min(vector.x, vector.z),
      Math.min(vector.y, vector.w),
      Math.max(vector.x, vector.z),
      Math.max(vector.y, vector.w)
    );
  }

  private List<ISelectable> selected = new ArrayList<ISelectable>();

  private void makeSelection(Vector4f a) {
    selected.clear();
    Vector4f normalizedSelectionBoxScreen = sortVector(a);
    Vector2f selectionBoxWorldMin = camera.screen2world(normalizedSelectionBoxScreen.x, normalizedSelectionBoxScreen.y);
    Vector2f selectionBoxWorldMax = camera.screen2world(normalizedSelectionBoxScreen.z, normalizedSelectionBoxScreen.w);

    List<ISelectable> selectables = getAll(ISelectable.class);

    for(ISelectable thing : selectables) {
      Vector4f thingBox = thing.getWorldBox();
      if(rectanglesIntersect(
        selectionBoxWorldMin.x, selectionBoxWorldMin.y,
        selectionBoxWorldMax.x, selectionBoxWorldMax.y,
        thingBox.x, thingBox.y,
        thingBox.z, thingBox.w
      )) {
        selected.add(thing);
      }
    }

    animation = 0;
    broadcastSelectionChanged();

  }

  private void broadcastSelectionChanged() {
    for(ISelectionChangeListener listener : listeners) {
      listener.selectionChanged(selected);
    }
  }

  public void updateSelection(List<ISelectable> newSelection) {
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
  public Vector4f getBox() {
    return new Vector4f(0, 0, 1000, 1000);
  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public void mouseDown(int button) {
    if(!active) return;
    
    if(button == 0) {
      if(initialCoords == null) {
        initialCoords = new Vector2f(App.mouseX, App.mouseY);
      }
    }
  }

  @Override
  public void mouseUp(int button) {
    if(initialCoords != null && button == 0) {

      makeSelection(new Vector4f(
        initialCoords.x,
        initialCoords.y,
        App.mouseX,
        App.mouseY
      ));

      initialCoords = null;
    }
  }
}
