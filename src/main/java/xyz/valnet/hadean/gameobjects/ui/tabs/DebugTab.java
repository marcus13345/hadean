package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.engine.App;
import xyz.valnet.hadean.HadeanGame;
import xyz.valnet.hadean.gameobjects.BottomBar;

public class DebugTab extends Tab {

  private int width = 250;
  private static Runtime runtime = Runtime.getRuntime();

  @Override
  public String getTabName() {
    return "Toggle Debug";
  }

  @Override
  protected void onClose() {}

  @Override
  protected void onOpen() {}

  @Override
  protected void gui() {
    if(!shouldRender()) return;

    window(animate(1200, 1024 - width), 0, width, 576 - BottomBar.bottomBarHeight + 1, () -> {
      text("Debug");
      space(8);

      text(System.getProperty("java.runtime.name"));
      space(8);
      text(System.getProperty("java.version"));
      space(8);

      long allocated = runtime.totalMemory();
      long max = runtime.maxMemory();

      text("MEMORY: " + (int)((allocated / (double)max) * 100) + "% (" + (allocated / (1024 * 1024)) + "/" + (max / (1024 * 1024)) + "MB)");
      space(8);

      if(button("Debug: " + (HadeanGame.debugView ? "on" : "off"))) {
        HadeanGame.debugView = !HadeanGame.debugView;
      }
    });
    
  }

}
