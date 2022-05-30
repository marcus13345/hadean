package xyz.valnet.hadean.gameobjects.tabs;

import static xyz.valnet.engine.util.Math.lerp;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.inputlayer.BuildLayer;
import xyz.valnet.hadean.gameobjects.inputlayer.Selection;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.SmartBoolean;

public class JobBoardTab extends Tab implements ISelectionChangeListener {
  
  private Selection selection;
  private JobBoard jobBoard;

  private SmartBoolean opened;
  private float progress = 0f;
  private float width = 200;

  private int padding = 10;

  private int x, y;

  @Override
  public void render() {
    Drawing.setLayer(Layers.GENERAL_UI);
    float left = lerp(-width - padding, padding, progress);
    Assets.uiFrame.draw((int) left, padding, (int) width, 576 - padding * 2 - BottomBar.bottomBarHeight);
    Assets.font.drawString(jobBoard.details(), (int) left + padding, padding * 2);
  }

  @Override
  public void start() {
    super.start();
    selection = get(Selection.class);
    jobBoard = get(JobBoard.class);
    
    opened = new SmartBoolean(false, new SmartBoolean.IListener() {
    });

    if(selection != null) {
      selection.subscribe(this);
    }
  }

  @Override
  public void update(float dTime) {
    progress = lerp(progress, opened.value() ? 1 : 0, 0.05f);
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    if(selected.isEmpty()) return;
    opened.set(false);
  }

  @Override
  public void evoke() {
    opened.toggle();

    if(opened.value()) {
      selection.updateSelection(new ArrayList<ISelectable>());
    }
  }

  @Override
  public String getTabName() {
    return "Jobs";
  }
}
