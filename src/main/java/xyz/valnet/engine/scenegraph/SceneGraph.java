package xyz.valnet.engine.scenegraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import xyz.valnet.engine.App;

public abstract class SceneGraph implements IScene {
  protected final List<GameObject> objects = new ArrayList<GameObject>();
  private final List<GameObject> newObjects = new ArrayList<GameObject>();
  private final List<GameObject> removeObjects = new ArrayList<GameObject>();

  private IMouseCaptureArea hoveredMouseListener = null;
  
  public <T> T get(Class<T> clazz) {
    for(GameObject obj : objects) {
      if(clazz.isInstance(obj)) {
        return clazz.cast(obj);
      }
    }
    return null;
  }

  public <T> List<T> getAll(Class<T> clazz) {
    List<T> stuff = new ArrayList<T>();
    for(GameObject obj : objects) {
      if(clazz.isInstance(obj)) {
        stuff.add(clazz.cast(obj));
      }
    }
    return stuff;
  }

  @Override
  public void update(float dTime) {
    // ADD OBJECTS
    if(!newObjects.isEmpty()) {
      List<GameObject> added = new ArrayList<GameObject>();

      for(GameObject obj : newObjects) {
        objects.add(obj);
        added.add(obj);
      }
      newObjects.clear();

      for(GameObject obj : added) {
        obj.start();
      }
    }

    // REMOVE OBJECTS
    if(!removeObjects.isEmpty()) {
      for(GameObject obj : removeObjects) {
        objects.remove(obj);
      }
      removeObjects.clear();
    }

    // TICK OBJECTS
    for(GameObject obj : objects) {
      obj.update(dTime);
    }

    // DO MOUSE UPDATES!
    List<IMouseCaptureArea> mouseListeners = getAll(IMouseCaptureArea.class);
    mouseListeners.sort(new Comparator<IMouseCaptureArea>() {
      @Override
      public int compare(IMouseCaptureArea a, IMouseCaptureArea b) {
        float al = a.getLayer();
        float bl = b.getLayer();
        return al < bl ? 1 : bl < al ? -1 : 0;
      }
    });
    for(IMouseCaptureArea listener : mouseListeners) {
      boolean currentlyEntered = listener.getBox().contains(App.mouseX, App.mouseY);
      if(currentlyEntered) {
        if(listener != hoveredMouseListener) {
          if(hoveredMouseListener != null) {
            hoveredMouseListener.mouseLeave();
          }
          hoveredMouseListener = listener;
          listener.mouseEnter();
        }
        break;
      } else if(listener == hoveredMouseListener) {
        // this is the one that is currently hovered, but it isnt!
        // turn that shit OFF
        hoveredMouseListener.mouseLeave();
        hoveredMouseListener = null;
      }
    }
  }

  @Override
  public void enable() {
    this.construct();

    for(GameObject obj : objects) {
      obj.link(this);
    }

    for(GameObject obj : objects) {
      obj.start();
    }
  }

  @Override
  public void render() {
    for(GameObject obj : objects) {
      obj.render();
    }
  }

  protected abstract void construct();
  
  
  @Override
  public void disable() {
    objects.clear();
  }

  public void add(GameObject obj) {
    newObjects.add(obj);
    obj.link(this);
  }

  public void remove(GameObject obj) {
    removeObjects.add(obj);
  }

  public boolean inScene(GameObject gameObject) {
    return objects.contains(gameObject);
  }

  @Override
  public void mouseDown(int button) {
    for(IMouseCaptureArea iml : getAll(IMouseCaptureArea.class)) {
      iml.mouseDown(button);
    }
  }

  @Override
  public void mouseUp(int button) {
    for(IMouseCaptureArea iml : getAll(IMouseCaptureArea.class)) {
      iml.mouseUp(button);
    }
  }
}
