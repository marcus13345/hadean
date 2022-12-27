package xyz.valnet.engine.scenegraph;

import java.util.List;

public class GameObject implements IRenderable, ITickable {
  private SceneGraph scene;

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

  public void start() {}

  protected void remove(GameObject obj) {
    scene.remove(obj);
  }
}
