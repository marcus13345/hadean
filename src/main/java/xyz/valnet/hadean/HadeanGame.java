package xyz.valnet.hadean;

import xyz.valnet.engine.App;
import xyz.valnet.engine.Game;
import xyz.valnet.engine.math.Matrix4f;
import xyz.valnet.engine.math.Vector4f;

import xyz.valnet.hadean.scenes.MenuScene;
import xyz.valnet.hadean.util.Assets;


public class HadeanGame extends Game {
  public static final HadeanGame Hadean = new HadeanGame();

  public static void main(String[] args) {
    new App(Hadean).run();
  }

  @Override
  public void start() {
    Assets.flat.pushColor(Vector4f.one);
    changeScene(new MenuScene());
  }

  @Override
  public void render() {
    super.render();
    renderDebugInfo();
  }

  private Runtime runtime = Runtime.getRuntime();
  private static Vector4f fontColor = new Vector4f(0, 1, 1, 1);

  private void renderDebugInfo() {
    long allocated = runtime.totalMemory();
    long max = runtime.maxMemory();

    Assets.flat.pushColor(Vector4f.black);
    Assets.font.drawString("FPS: " + Math.round(averageFPS) + "/" + measuredFPS + " | AVG/MEASURED", 1, 1);
    Assets.font.drawString("Mouse: <" + App.mouseX + ", " + App.mouseY + ">", 1, 17);
    Assets.font.drawString("MEMORY: " + (int)((allocated / (double)max) * 100) + "% (" + (allocated / (1024 * 1024)) + "/" + (max / (1024 * 1024)) + "MB)", 1, 33);
    Assets.font.drawString("", 1, 49);
    Assets.font.drawString("", 1, 65);
    Assets.font.drawString("", 1, 81);

    Assets.flat.swapColor(fontColor);
    Assets.font.drawString("FPS: " + Math.round(averageFPS) + "/" + measuredFPS + " | AVG/MEASURED", 0, 0);
    Assets.font.drawString("Mouse: <" + App.mouseX + ", " + App.mouseY + ">", 0, 16);
    Assets.font.drawString("MEMORY: " + (int)((allocated / (double)max) * 100) + "% (" + (allocated / (1024 * 1024)) + "/" + (max / (1024 * 1024)) + "MB)", 0, 32);
    Assets.font.drawString("", 0, 48);
    Assets.font.drawString("", 0, 64);
    Assets.font.drawString("", 0, 80);
    
    Assets.flat.popColor();
  }

  // receive the updated matrix every frame for the actual window.
  @Override
  public void updateViewMatrix(Matrix4f matrix) {
    Assets.flat.setMatrices(matrix);
  }

}
