package xyz.valnet.hadean.gameobjects.ui.tabs;

import static xyz.valnet.engine.util.Math.lerp;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class JobBoardTab extends Tab implements ISelectionChangeListener {
  
  private SelectionLayer selection;
  private JobBoard jobBoard;

  private boolean opened;
  private float progress = 0f;
  private float width = 200;

  private int padding = 10;

  @Override
  public void render() {
    Drawing.setLayer(Layers.GENERAL_UI);
    float left = lerp(-width - padding, padding, progress);
    Assets.uiFrame.draw((int) left, padding, (int) width, 576 - padding * 2 - BottomBar.bottomBarHeight);
    Assets.font.drawString(jobBoard.details(), (int) left + padding, padding * 2);
  }

  @Override
  protected void connect() {
    super.connect();
    selection = get(SelectionLayer.class);
    jobBoard = get(JobBoard.class);
  }

  @Override
  public void start() {
    super.start();
    opened = false;
    if(selection != null) selection.subscribe(this);

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
    return "Jobs";
  }

  @Override
  protected void onClose() {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void onOpen() {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void gui() {
    // TODO Auto-generated method stub
    
  }
}
