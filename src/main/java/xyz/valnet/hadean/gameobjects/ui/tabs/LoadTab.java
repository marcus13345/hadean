package xyz.valnet.hadean.gameobjects.ui.tabs;

public class LoadTab extends Tab {

  @Override
  public void evoke() {
    load();
  }

  @Override
  public String getTabName() {
    return "Load";
  }
  
}
