package xyz.valnet.hadean.gameobjects.tabs;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.interfaces.IBottomBarItem;

public abstract class Tab extends GameObject implements IBottomBarItem {

  private BottomBar bottombar;

  @Override
  public void start() {
    bottombar = get(BottomBar.class);
    bottombar.registerButton(this);
  }
}
