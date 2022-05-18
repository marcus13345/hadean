package xyz.valnet.engine.scenegraph;

public class GameObject implements IRenderable, ITickable {
  // private IScene scene;

  public GameObject(IScene scene) {
    // this.scene = scene;
  }

  @Override
  public void render() {}

  @Override
  public void tick(float dTime) {}
}
