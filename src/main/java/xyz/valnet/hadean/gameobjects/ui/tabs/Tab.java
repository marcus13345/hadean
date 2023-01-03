package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.interfaces.IBottomBarItem;

public abstract class Tab extends GameObject implements IBottomBarItem, ITransient {

  private BottomBar bottombar;

  @Override
  protected void connect() {
    bottombar = get(BottomBar.class);
  }
  
  @Override
  protected void start() {
    bottombar.registerButton(this);
  }
}
