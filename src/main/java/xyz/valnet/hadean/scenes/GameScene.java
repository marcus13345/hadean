package xyz.valnet.hadean.scenes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import xyz.valnet.engine.App;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IScene;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Pawn;
import xyz.valnet.hadean.gameobjects.Selection;
import xyz.valnet.hadean.gameobjects.SelectionUI;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.tabs.ArchitectTab;
import xyz.valnet.hadean.gameobjects.tabs.MenuTab;
import xyz.valnet.hadean.input.IMouseListener;

// TODO BIG IDEAS
// have caches of types that ill need (Like IMouseListener)
// and the add method, simply puts things into the newObjects
// then at the beginning of each frame, they are all loaded.
// then, even userspace scenes can add all their objects in 
// one fell swoop and the timings will be maintained
// IE: all objects will be linked and able to be `get`ed
// at their start call! :D

public class GameScene implements IScene {

  // generic
  private List<GameObject> objects = new ArrayList<GameObject>();
  private List<GameObject> newObjects = new ArrayList<GameObject>();
  private List<GameObject> removeObjects = new ArrayList<GameObject>();
  // private List<IRenderable> renderables = new ArrayList<IRenderable>();

  private IMouseListener hoveredMouseListener = null;

  // specific

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
  public void render() {
    for(GameObject obj : objects) {
      obj.render();
    }
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
    List<IMouseListener> mouseListeners = getAll(IMouseListener.class);
    mouseListeners.sort(new Comparator<IMouseListener>() {
      @Override
      public int compare(IMouseListener a, IMouseListener b) {
        int al = a.getLayer();
        int bl = b.getLayer();
        return al < bl ? 1 : bl < al ? -1 : 0;
      }
    });
    for(IMouseListener listener : mouseListeners) {
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
    objects.add(new Terrain());
    for(int i = 0; i < 5; i ++) {
      objects.add(new Pawn());
    }
    
    objects.add(new Camera());
    objects.add(new Selection());
    objects.add(new SelectionUI());
    objects.add(new BottomBar());
    objects.add(new ArchitectTab());
    objects.add(new MenuTab());
    
    for(GameObject obj : objects) {
      obj.link(this);
    }

    for(GameObject obj : objects) {
      obj.start();
    }
  }

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
    for(IMouseListener iml : getAll(IMouseListener.class)) {
      iml.mouseDown(button);
    }
  }

  @Override
  public void mouseUp(int button) {
    for(IMouseListener iml : getAll(IMouseListener.class)) {
      iml.mouseUp(button);
    }
  }
  
}
