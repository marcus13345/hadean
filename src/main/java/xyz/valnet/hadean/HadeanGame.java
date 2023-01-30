package xyz.valnet.hadean;

import xyz.valnet.engine.App;
import xyz.valnet.engine.Game;
import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Matrix4f;
import xyz.valnet.hadean.scenes.GameScene;
import xyz.valnet.hadean.util.Assets;

public class HadeanGame extends Game {
  public static final HadeanGame Hadean = new HadeanGame();

  public static boolean debugView = false;

  public static void main(String[] args) {
    new App(Hadean).run();
  }

  @Override
  public void start() {
    Assets.flat.pushColor(Color.white);
    changeScene(new GameScene());
  }

  @Override
  public void render() {
    Drawing.setLayer(0);
    super.render();

    if(!debugView) return;
  }

  public float getAverageFPS() {
    return averageFPS;
  }

  public int getMeasuredFPS() {
    return measuredFPS;
  }

  // receive the updated matrix every frame for the actual window.
  @Override
  public void updateViewMatrix(Matrix4f matrix) {
    Assets.flat.setMatrices(matrix);
  }
}
