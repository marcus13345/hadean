package xyz.valnet.engine.scenegraph;

public interface IScene {
    public void render();
    public void update(float dTime);

    public void enable();
    public void disable();
}
