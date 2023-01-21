package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.interfaces.IBottomBarItem;

public abstract class Tab extends ImmediateUI implements IBottomBarItem, ITransient {

  private BottomBar bottombar;

  @Override
  protected void connect() {
    bottombar = get(BottomBar.class);
  }
  
  @Override
  protected void start() {
    bottombar.registerButton(this);
  }

  @Override
  public boolean isButtonClickSilent() {
    return false;
  }

  public void gui() {}
}
