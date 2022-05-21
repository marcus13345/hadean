package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.util.Assets;

public class SelectionUI extends GameObject implements ISelectionChangeListener {

  private List<ISelectable> selected = new ArrayList<ISelectable>();
  private HashMap<String, Integer> selectedTypes = new HashMap<String, Integer>();

  private Selection selectionManager;

  public void start() {
    selectionManager = get(Selection.class);
    selectionManager.subscribe(this);
  }

  public void render() {
    if(selected.isEmpty()) return;

    Assets.uiFrame.draw(10, 366, 300, 200);

    int i = 0;
    for(String name : selectedTypes.keySet()) {
      int n = selectedTypes.get(name);
      Assets.font.drawString("" + n + "x " + name, 26, 376 + 16 * i);
      i ++;
    }
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    this.selected = selected;
    
    selectedTypes.clear();
    for(ISelectable selectable : selected) {
      String name = selectable.getClass().getName();
      if(selectedTypes.containsKey(name)) {
        selectedTypes.replace(name, selectedTypes.get(name) + 1);
      } else {
        selectedTypes.put(name, 1);
      }
    }
  }
}
