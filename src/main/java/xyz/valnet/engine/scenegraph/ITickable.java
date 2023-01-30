package xyz.valnet.engine.scenegraph;

public interface ITickable {
  public void update(float dTime);
  public default void fixedUpdate(float dTime) {}
}
