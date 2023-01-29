package xyz.valnet.hadean.gameobjects.ui.tabs;

public class MenuTab extends Tab {

  private int width = 300;
  private int height = 6 * 32 + 9 * 8;

  @Override
  protected void onClose() {
    
  }

  @Override
  protected void onOpen() {
    
  }

  @Override
  protected void gui() {
    window(1024 / 2 - width / 2, animate(-height - 50, 576 / 2 - height / 2), width, height, () -> {
      if(button("Options")) {
        
      }
      space(8);
      if(button("Debug")) {
        get(DebugTab.class).open();
      }
      space(8);
      if(button("Save")) {
        save();
      }
      space(8);
      if(button("Load")) {
        load();
      }
      space(24);
      if(button("Main Menu")) {
        
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

}