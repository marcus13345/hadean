package xyz.valnet.hadean.gameobjects.ui.tabs;

import java.util.LinkedList;
import java.util.List;

import xyz.valnet.engine.scenegraph.IKeyboardListener;
import xyz.valnet.hadean.HadeanGame;
import xyz.valnet.hadean.gameobjects.ui.BottomBar;

public class DebugTab extends Tab implements IKeyboardListener {

  private int width = 250;
  private static Runtime runtime = Runtime.getRuntime();

  @Override
  public String getTabName() {
    return "Debug";
  }

  @Override
  protected void onClose() {}

  @Override
  protected void onOpen() {}

  @Override
  protected void gui() {
    if(!shouldRender()) return;

    window(0, animate(-200, 0), 1024 - width + 1, 176, () -> {
      for(int i = 10; i > logs.size(); i --) {
        text(" ");
      }
      for(String str : logs) {
        text(str);
      }
    });

    window(animate(1050, 1024 - width), 0, width, 576 - BottomBar.bottomBarHeight + 1, () -> {
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

  private static List<String> logs = new LinkedList<String>();

  public static void log(String str) {
    logs.add(str);
    while(logs.size() > 10) {
      logs.remove(0);
    }
  }

  public static void log(Object obj) {
    log(obj.toString());
  }

  @Override
  public void keyPress(int code) {
    if(code == 96) { // tilde
      evoke();
    }
  }

  @Override
  public void keyRelease(int code) {}

}
