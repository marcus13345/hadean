package xyz.valnet.engine.scenegraph;

import java.io.Serializable;
import java.util.List;

public class GameObject implements IRenderable, ITickable, Serializable {
  private transient SceneGraph scene;

  private boolean created = false;

  public void link(SceneGraph scene) {
    this.scene = scene;
  }

  public boolean inScene() {
    return scene != null && scene.inScene(this);
  }

  protected void dumpScene() {
    scene.dump();
  }

  protected <T> T get(Class<T> clazz) {
    return this.scene.get(clazz);
  }

  protected <T> List<T> getAll(Class<T> clazz) {
    return this.scene.getAll(clazz);
  }

  protected final <T extends GameObject> T add(T obj) {
    if(obj.inScene()) {
      return obj;
    }
    scene.add(obj);
    return obj;
  }

  @Override
  public void render() {}
  
  @Override
  public void renderAlpha() {}

  @Override
  public void update(float dTime) {}

  // connect is solely for ensuring links to other objects. get() and getAll()
  protected void connect() {}
  // create is guaranteed to only run once for an object, even after save/load
  protected void create() {} 
  // start is called any time the object is added to a scene
  protected void start() {}

  public final void addedToScene() {
    connect();
    if(!created) {
      create();
      created = true;
    }
    start();
  }

  protected void remove(GameObject obj) {
    scene.remove(obj);
  }

  protected void save() {
    scene.queueSave();
  }

  protected void load() {
    scene.queueLoad();
  }
}
