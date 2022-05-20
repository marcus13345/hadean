package xyz.valnet.engine.scenegraph;

import xyz.valnet.hadean.scenes.GameScene;

public class GameObject implements IRenderable, ITickable {
  private final GameScene scene;

  public GameObject(GameScene scene) {
    this.scene = scene;
  }

  protected <T> T get(Class<T> clazz) {
    return this.scene.get(clazz);
  }

  @Override
  public void render() {}

  @Override
  public void tick(float dTime) {}

  public void start() {}
}
