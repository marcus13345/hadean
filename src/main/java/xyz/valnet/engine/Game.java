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

  public void update() {
    scene.update(0);

    long nanoTime = System.nanoTime();

    // average framerate
    averageFPS = lerp(averageFPS, 1_000_000_000f / (nanoTime - lastFrame), (nanoTime - lastFrame) / 1_000_000_000f);
    lastFrame = nanoTime;

    framesSinceKeyframe ++;
    if(nanoTime > lastKeyframe + 1_000_000_000) {
      measuredFPS = framesSinceKeyframe;
      framesSinceKeyframe = 0;
      lastKeyframe += 1_000_000_000;
    }
  }

  public abstract void updateViewMatrix(Matrix4f matrix);
}
