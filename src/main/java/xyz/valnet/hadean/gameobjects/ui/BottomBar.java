package xyz.valnet.hadean.gameobjects.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.input.IButtonListener;
import xyz.valnet.hadean.input.SimpleButton;
import xyz.valnet.hadean.interfaces.IBottomBarItem;
import xyz.valnet.hadean.util.Layers;

public class BottomBar extends GameObject implements IButtonListener, ITransient {
  public static final int bottomBarHeight = 32;
  private int screenWidth = 1024;

  private Map<Button, IBottomBarItem> btnItemTable = new HashMap<Button, IBottomBarItem>();

  private List<IBottomBarItem> items = new ArrayList<IBottomBarItem>();

  @Override
  public void start() {
    items.clear();
    clearButtons();
  }

  public void registerButton(IBottomBarItem newItem) {
    clearButtons();
    items.add(newItem);
    int n = items.size();

    int i = 0;
    for(IBottomBarItem item : items) {
      int l = (int)((i / (float) n) * screenWidth);
      int r = (int)(((i + 1) / (float) n) * screenWidth);

      int w = r - l;
      Button btn = new SimpleButton(item.getTabName(), l, 576 - bottomBarHeight, w, bottomBarHeight, Layers.BOTTOM_BAR);
      if(item.isButtonClickSilent()) btn = btn.setClickSound(false);
      btn.registerClickListener(this);
      add(btn);

      btnItemTable.put(btn, item);

      i++;
    }
  }

  private void clearButtons() {
    for(Button btn : btnItemTable.keySet()) {
      remove(btn);
    }
    btnItemTable.clear();
  }

  @Override
  public void click(Button target) {
    if(btnItemTable.containsKey(target)) {
      btnItemTable.get(target).evoke();
    }
  }
}
