package xyz.valnet.engine.scenegraph;

import java.io.Serializable;
import java.util.List;

import xyz.valnet.engine.scenegraph.SceneGraph.GameObjectCallback;
import xyz.valnet.hadean.util.Pair;

public class GameObject implements IRenderable, ITickable, Serializable {
  private transient SceneGraph scene;

  private boolean created = false;

  public void link(SceneGraph scene) {
    this.scene = scene;
    this.ready();
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

  // call order goes from top to bottom \/\/\/
  // ready is called before scene linkage, and serves to initialize
  // values that may be needed before incoming requests.
  protected void ready() {}
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

  protected boolean getKey(int key) {
    return scene.getKey(key);
  }

  protected boolean isPaused() {
    return scene.isPaused();
  }

  protected Pair<Float, Integer> getFPS() {
    return scene.getFPS();
  }

  protected void onAddGameObject(GameObjectCallback listener) {
    scene.registerAddListener(listener);
  }

  protected void onRemoveGameObject(GameObjectCallback listener) {
    scene.registerRemoveListener(listener);
  }
}
