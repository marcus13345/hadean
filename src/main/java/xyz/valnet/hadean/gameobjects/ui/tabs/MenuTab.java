package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.engine.scenegraph.IPauser;

public class MenuTab extends Tab implements IPauser {

  private int width = 300;
  private int height = 6 * 32 + 1 * 16 + 6 * 8 + 2 * 24;

  @Override
  protected void onClose() {
    
  }

  @Override
  protected void onOpen() {
    
  }

  @Override
  protected void gui() {
    window(1024 / 2 - width / 2, animate(-height - 50, 576 / 2 - height / 2), width, height, () -> {
      text("         ===   Paused   ===");
      space(8);
      if(button("Resume")) {
        close();
      }
      space(24);
      if(button("Options")) {
        
      }
      space(8);
      if(button("Save")) {
        save();
        close();
      }
      space(8);
      if(button("Load")) {
        load();
        close();
      }
      space(24);
      if(button("Quit to Menu")) {
        
      }
      space(8);
      if(button("Quit to Desktop")) {
        
      }
    });
  }

  @Override
  public String getTabName() {
    return "Menu";
  }

  @Override
  public boolean isPaused() {
    return opened;
  }

}