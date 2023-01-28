package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.graphics.IModalUI;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.ui.tabs.BuildTab;
import xyz.valnet.hadean.gameobjects.ui.tabs.Tab;
import xyz.valnet.hadean.util.Assets;

public class ExclusivityManager extends GameObject implements ITransient {
  private IModalUI current = null;

  private boolean switching = false;

  private IModalUI defaultTab = null;
  
  public void switchTo(IModalUI tab) {
    if(tab == current) return;
    if(tab == null) {
      closeCurrent();
      return;
    }
    Assets.sndBubble.play();
    switching = true;
    if(current != null) current.close();
    current = tab;
    current.open();
    switching = false;
  }

  public void closeCurrent() {
    if(switching) return;
    if(current == null) return;
    Assets.sndCancel.play();
    current.close();
    current = null;
  }

  public void backOrDefault() {
    if(current == null) switchTo(defaultTab);
    else current.back();
  }

  @Override
  protected void connect() {
    defaultTab = get(BuildTab.class);
  }

}
