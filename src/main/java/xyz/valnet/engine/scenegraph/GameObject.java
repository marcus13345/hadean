package xyz.valnet.engine.scenegraph;

import java.util.List;

import xyz.valnet.hadean.scenes.GameScene;

public class GameObject implements IRenderable, ITickable {
  private GameScene scene;

  public void link(GameScene scene) {
    this.scene = scene;
  }

  public boolean inScene() {
    return scene.inScene(this);
  }

  protected <T> T get(Class<T> clazz) {
    return this.scene.get(clazz);
  }

  protected <T> List<T> getAll(Class<T> clazz) {
    return this.scene.getAll(clazz);
  }

  protected final void add(GameObject obj) {
    scene.add(obj);
  }

  @Override
  public void render() {}

  @Override
  public void tick(float dTime) {}

  public void start() {}

  public void remove(GameObject obj) {
    scene.remove(obj);
  }
}
