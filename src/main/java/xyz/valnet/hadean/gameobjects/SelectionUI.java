package xyz.valnet.hadean.gameobjects;

import static xyz.valnet.engine.util.Math.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xyz.valnet.engine.graphics.IModalUI;
import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.Constants;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.gameobjects.ui.ExclusivityManager;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.Detail;

public class SelectionUI extends ImmediateUI implements ISelectionChangeListener, ITransient, IModalUI {

  private class SelectedByType extends HashMap<Class<? extends ISelectable>, List<ISelectable>> {}

  private int selectedCount = 0;
  private String properName;
  private String genericName;
  private List<ISelectable> selected = new ArrayList<ISelectable>();
  private transient SelectedByType selectedByType = new SelectedByType();
  private transient Set<Action> actions = new HashSet<Action>();

  private SelectionLayer selectionManager;

  private final int width = 300, height = 200;
  private final int padding = 0;
  
  private boolean opened = false;
  private float openness = 0f;

  private ExclusivityManager exclusivityManager;

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
    openness = lerp(openness, opened ? 1 : 0, dTime / Constants.animationSpeed);
    if(newSelection != null) {
      selectionManager.updateSelection(newSelection);
      newSelection = null;
    }
    if(!opened && openness < 0.0001f && selected.size() > 0) {
      resetCache();
    }
  }

  private void resetCache() {
    selected = new ArrayList<ISelectable>();
    selectedByType.clear();
    selectedCount = selected.size();
    actions.clear();
  }

  @Override
  public void selectionChanged(List<ISelectable> newSelection) {

    if(selected.size() != 0 && newSelection.size() != 0) {
      Assets.sndBubble.play();
    }

    if(newSelection.size() == 0) {
      close();
      return;
    }

    resetCache();
    selected = newSelection;
    selectedCount = selected.size();

    for(ISelectable selectable : newSelection) {
      Class<? extends ISelectable> clazz = selectable.getClass();
      
      properName = selectable.getName();
      genericName = selectable.getGenericName();
      for(Action action : selectable.getActions()) {
        actions.add(action);
      }
      
      if(!selectedByType.containsKey(clazz)) {
        selectedByType.put(clazz, new ArrayList<ISelectable>());
      }

      selectedByType.get(clazz).add(selectable);
    }

    open();
  }

  @Override
  public float getLayer() {
    return Layers.GENERAL_UI;
  }

  private int animate(int a, int b) {
    return (int) Math.round(lerp(a, b, openness));
  }
  
  @Override
  protected void gui() {
    // if(selected.isEmpty()) return;
    if(!opened && openness <= 0.0001f) return;

    // main window
    window(animate(-width - 50, 0), 576 - height - BottomBar.bottomBarHeight + 1, width, height, () -> {
      if(selectedByType.size() == 1) {
        if(selectedCount == 1) {
          text(properName);
          space(8);
          group(() -> {
            Detail[] details = selected.get(0).getDetails();
            if(details.length == 0) {
              text("No details available.");
            } else for(Detail detail : details) {
              text(detail.toString(15));
            }
          });
        } else {
          text("" + selectedCount + "x " + genericName);
        }
      } else {
        text(this.selected.size() + " items selected");
  
        for(var entry : selectedByType.entrySet()) {
          space(8);
          List<ISelectable> list = entry.getValue();
          int count = list.size();
          if(count <= 0) continue;
          String name = list.get(0).getGenericName();
  
          if(button(name, count + "x " + name)) {
            newSelection = list;
          }
        }
      }
    });

    // actions
    window(width - 1, animate(576 + 50, 576 - 48 - BottomBar.bottomBarHeight + 1), 1024 - width + 1, 48, () -> {
      if(selectedByType.size() == 1) {
        horizontal(() -> {
          for(Action action : actions) {
            if(button(action.name)) {
              for(ISelectable selectable : selected) {
                selectable.runAction(action);
              }
            }
            space(8);
          }
        });
      } else {
        space(8);
        text("  Select an Item Type");
      }
    });

  }

  @Override
  public void open() {
    if(opened) return;
    opened = true;
    exclusivityManager.switchTo(this);
  }

  @Override
  public void close() {
    if(!opened) return;
    opened = false;
    exclusivityManager.closeCurrent();
    selectionManager.clearSelection();
  }

  @Override
  protected void connect() {
    super.connect();
    exclusivityManager = get(ExclusivityManager.class);
  }
}
