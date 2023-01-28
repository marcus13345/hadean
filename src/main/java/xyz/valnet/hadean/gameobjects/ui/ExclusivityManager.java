package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.ui.tabs.Tab;
import xyz.valnet.hadean.util.Assets;

public class ExclusivityManager extends GameObject {
  private Tab current = null;

  private boolean switching = false;
  
  public void switchTo(Tab tab) {
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
    Assets.sndCancel.play();
    current.close();
    current = null;
  }

}
