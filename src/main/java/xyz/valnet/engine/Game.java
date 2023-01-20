package xyz.valnet.engine;

import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.math.Matrix4f;
import xyz.valnet.engine.scenegraph.IScene;

public abstract class Game {
  private IScene scene;
  
  protected float averageFPS = 0;
  protected int measuredFPS = 0;

  private int framesSinceKeyframe = 0;
  private long lastFrame = System.nanoTime();
  private long lastKeyframe = System.nanoTime();
  
  public abstract void start();

  public void changeScene(IScene scene) {
    if(this.scene != null) {
      this.scene.disable();
    }
    scene.enable();
    this.scene = scene;
  }
  
  public void render() {
    scene.render();
  }

  private static final float ns240 = 1_000_000_000f / 240f;
  protected float dTime = 0;

  public void update() {
    long nanoTime = System.nanoTime();
    float elapsed = nanoTime - lastFrame;
    dTime = elapsed / ns240;
    lastFrame = nanoTime;

    scene.update(dTime);

    // average framerate
    averageFPS = lerp(averageFPS, 1_000_000_000f / (elapsed), (elapsed) / 1_000_000_000f);

    framesSinceKeyframe ++;
    if(nanoTime > lastKeyframe + 1_000_000_000) {
      measuredFPS = framesSinceKeyframe;
      framesSinceKeyframe = 0;
      lastKeyframe += 1_000_000_000;
    }
  }

  public abstract void updateViewMatrix(Matrix4f matrix);

  public final void mouseDown(int button) {
    scene.mouseDown(button);
  }
  
  public final void mouseUp(int button) {
    scene.mouseUp(button);
  }

  public final void keyPress(int key) {
    scene.keyPress(key);
  }

  public final void keyRelease(int key) {
    scene.keyRelease(key);
  }

  public final void keyRepeat(int key) {
    scene.keyRepeat(key);
  }
}
