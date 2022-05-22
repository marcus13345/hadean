package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.input.IButtonListener;
import xyz.valnet.hadean.input.SimpleButton;

public class BottomBar extends GameObject implements IButtonListener {
  public static final int bottomBarHeight = 32;
  private int screenWidth = 1024;

  private Map<Button, IBottomBarItem> btnItemTable = new HashMap<Button, IBottomBarItem>();

  private List<IBottomBarItem> items = new ArrayList<IBottomBarItem>();

  @Override
  public void start() {
    items.clear();
    btnItemTable.clear();
  }

  public void registerButton(IBottomBarItem newItem) {
    btnItemTable.clear();
    items.add(newItem);
    int n = items.size();

    int i = 0;
    for(IBottomBarItem item : items) {
      int l = (int)((i / (float) n) * screenWidth);
      int r = (int)(((i + 1) / (float) n) * screenWidth);

      int w = r - l;
      Button btn = new SimpleButton(item.getTabName(), l, 576 - bottomBarHeight, w, bottomBarHeight);

      btn.registerClickListener(this);

      btnItemTable.put(btn, item);

      i++;
    }
  }

  @Override
  public void render() {
    for(Button btn : btnItemTable.keySet()) {
      btn.draw();
    }
  }
  @Override
  public void tick(float dTime) {
    for(Button btn : btnItemTable.keySet()) {
      btn.update();
    }
  }

  @Override
  public void click(Button target) {
    if(btnItemTable.containsKey(target)) {
      btnItemTable.get(target).evoke();
    }
  }
}
