package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.hadean.HadeanGame;

public class DebugTab extends Tab {

  @Override
  public void evoke() {
    HadeanGame.debugView = !HadeanGame.debugView;
  }

  @Override
  public String getTabName() {
    return "Toggle Debug";
  }

}
