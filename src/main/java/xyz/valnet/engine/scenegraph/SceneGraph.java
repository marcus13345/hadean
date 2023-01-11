package xyz.valnet.engine.scenegraph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import xyz.valnet.engine.App;

public abstract class SceneGraph implements IScene {
  protected final List<GameObject> objects = new ArrayList<GameObject>();
  private final List<GameObject> newObjects = new ArrayList<GameObject>();
  private final List<GameObject> removeObjects = new ArrayList<GameObject>();

  private IMouseCaptureArea hoveredMouseListener = null;

  private boolean loadFlag = false;
  private boolean saveFlag = false;

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
    }

    // REMOVE OBJECTS
    if(!removeObjects.isEmpty()) {
      for(GameObject obj : removeObjects) {
        objects.remove(obj);
      }
      removeObjects.clear();
    }

    if(saveFlag) save();
    if(loadFlag) load();

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
      obj.addedToScene();
    }
  }

  @Override
  public void render() {
    for(GameObject obj : objects) {
      obj.render();
    }
    for(GameObject obj : objects) {
      obj.renderAlpha();
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
    obj.addedToScene();
  }

  public void remove(GameObject obj) {
    removeObjects.add(obj);
  }

  public boolean inScene(GameObject gameObject) {
    return objects.contains(gameObject) || newObjects.contains(gameObject);
  }

  public void dump() {
    for(GameObject go : objects)
      System.out.println(go);
  }

  private void dump(List<GameObject> objects) {
    Map<Class<? extends GameObject>, Integer> count = new HashMap<Class<? extends GameObject>, Integer>();
    for(GameObject go : objects) {
      Class<? extends GameObject> clazz = go.getClass();
      if(!count.containsKey(clazz))
        count.put(clazz, 0);
      count.put(clazz, count.get(clazz) + 1);
    }
    for(Entry<Class<? extends GameObject>, Integer> entry : count.entrySet()) {
      System.out.println("" + entry.getValue() + "x " + entry.getKey().getSimpleName());
    }
  }

  private ArrayList<GameObject> getNonTransientObjects() {
    return new ArrayList<GameObject>(objects.stream()
        .filter(go -> !(go instanceof ITransient))
        .collect(Collectors.toList()));
  }

  private void save() {
    try {
      FileOutputStream file = new FileOutputStream("SAVE_DATA.TXT");
      ObjectOutputStream out = new ObjectOutputStream(file);
      ArrayList<GameObject> toSave = getNonTransientObjects();
      System.out.println("=== [ SAVING ] ===");
      dump(toSave);
      out.writeObject(toSave);
      out.close();
      file.close();
      System.out.println("=== [ SAVED ] ===");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("=== [ FAILED ] ===");
    }
    saveFlag = false;
  }

  @SuppressWarnings("unchecked")
  private void load() {
    try {
      FileInputStream file = new FileInputStream("SAVE_DATA.TXT");
      ObjectInputStream input = new CustomObjectDeserializer(file);
      List<GameObject> newObjects = (List<GameObject>) input.readObject();
      input.close();
      file.close();
      System.out.println("imported " + newObjects.size() + " objects");
      ArrayList<GameObject> toRemove = getNonTransientObjects();

      for(GameObject obj : toRemove) {
        objects.remove(obj);
      }

      objects.addAll(newObjects);

      for(GameObject obj : newObjects) obj.link(this);
      for(GameObject obj : newObjects) obj.addedToScene();

    } catch (Exception e) {
      e.printStackTrace();
    }
    loadFlag = false;
  }

  protected void queueSave() {
    saveFlag = true;
  }

  protected void queueLoad() {
    loadFlag = true;
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
