package xyz.valnet.engine.scenegraph;

public interface IScene {
    public void render();
    public void update(float dTime);

    public void scrollUp();
    public void scrollDown();

    public void mouseDown(int button);
    public void mouseUp(int button);

    public void keyPress(int key);
    public void keyRelease(int key);
    public void keyRepeat(int key);

    public void enable();
    public void disable();
}
