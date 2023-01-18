package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class SelectionUI extends ImmediateUI implements ISelectionChangeListener, ITransient {

  private class SelectedByType extends HashMap<Class<? extends ISelectable>, List<ISelectable>> {}

  private int selectedCount = 0;
  private String properName;
  private String genericName;
  private List<ISelectable> selected = new ArrayList<ISelectable>();
  private transient SelectedByType selectedByType = new SelectedByType();

  private SelectionLayer selectionManager;

  private final int width = 300, height = 200;
  private final int padding = 10;
  private final int actionButtonSize = 100;

  // this will be null normally, but set if
  // a button has been pressed to update the selection.
  // its a simple workaround to get rid of a concurrent
  // exception, where the buttons are attempting to
  // change while updating. 
  // TODO this could be fixed by delaying button clicks to the next frame.
  private List<ISelectable> newSelection = null;

  public void start() {
    selectionManager = get(SelectionLayer.class);
    selectionManager.subscribe(this);
  }

  @Override
  public void update(float dTime) {
    if(newSelection != null) {
      selectionManager.updateSelection(newSelection);
      newSelection = null;
    }
  }

  @Override
  public void selectionChanged(List<ISelectable> newSelection) {

    selected = newSelection;
    selectedByType.clear();
    selectedCount = newSelection.size();

    for(ISelectable selectable : newSelection) {
      Class<? extends ISelectable> clazz = selectable.getClass();
      
      properName = selectable.getName();
      genericName = selectable.getGenericName();
      
      if(!selectedByType.containsKey(clazz)) {
        selectedByType.put(clazz, new ArrayList<ISelectable>());
      }

      selectedByType.get(clazz).add(selectable);
    }
  }

  @Override
  public float getLayer() {
    return Layers.GENERAL_UI;
  }

  @Override
  protected void gui() {
    if(selected.isEmpty()) return;

    if(selectedByType.size() == 1) {
      if(selectedCount == 1) {
        text(properName + "\n ");
        group();
        Detail[] details = selected.get(0).getDetails();
        if(details.length == 0) {
          text("No details available.");
        } else for(Detail detail : details) {
          text(detail.toString(15));
        }
        groupEnd();
      } else {
        text("" + selectedCount + "x " + genericName);
      }
    } else {
      text(this.selected.size() + " items selected");
      text("");

      for(var entry : selectedByType.entrySet()) {
        List<ISelectable> list = entry.getValue();
        int count = list.size();
        if(count <= 0) continue;
        String name = list.get(0).getGenericName();

        if(button(name, count + "x " + name)) {
          newSelection = list;
        }
      }
    }
  }
}
