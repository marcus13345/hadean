package xyz.valnet.hadean.gameobjects.tabs;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Selection;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;

import static xyz.valnet.engine.util.Math.lerp;

public class ArchitectTab extends Tab implements ISelectionChangeListener {
  
  private Selection selection;

  private boolean opened = false;
  private float progress = 0f;
  private float width = 200;

  private int padding = 10;

  private float toRange(float n, float a, float b) {
    float l = b - a;
    float p = n * l;
    return a + p;
  }

  @Override
  public void render() {
    float x = toRange(progress, -width - padding, padding);
    Assets.uiFrame.draw((int) x, padding, (int) width, 576 - padding * 2 - BottomBar.bottomBarHeight);
  }

  @Override
  public void start() {
    super.start();
    selection = get(Selection.class);
    if(selection != null) {
      selection.subscribe(this);
    }
  }

  @Override
  public void update(float dTime) {
    progress = lerp(progress, opened ? 1 : 0, 0.05f);
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    if(selected.isEmpty()) return;
    opened = false;
  }

  @Override
  public void evoke() {
    opened = !opened;
    if(opened) {
      selection.updateSelection(new ArrayList<ISelectable>());
    }
  }

  @Override
  public String getTabName() {
    return "Build";
  }
}
