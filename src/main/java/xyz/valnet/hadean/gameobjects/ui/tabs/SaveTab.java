package xyz.valnet.hadean.gameobjects.ui.tabs;

public class SaveTab extends Tab {

  @Override
  public void evoke() {
    save();
  }

  @Override
  public String getTabName() {
    return "Save";
  }
  
}
