package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.input.IButtonListener;
import xyz.valnet.hadean.util.Assets;

public class SelectionUI extends GameObject implements ISelectionChangeListener, IButtonListener {

  private List<ISelectable> selected = new ArrayList<ISelectable>();
  private HashMap<String, Integer> selectedTypes = new HashMap<String, Integer>();
  private HashMap<String, Button> narrowButtons = new HashMap<String, Button>();
  private HashMap<Button, List<ISelectable>> narrowBuckets = new HashMap<Button, List<ISelectable>>();

  private Selection selectionManager;

  public void start() {
    selectionManager = get(Selection.class);
    selectionManager.subscribe(this);
  }

  public void render() {
    if(selected.isEmpty()) return;

    Assets.uiFrame.draw(10, 366, 300, 200);

    // int i = 0;
    // for(String name : selectedTypes.keySet()) {
    //   int n = selectedTypes.get(name);
    //   Assets.font.drawString("" + n + "x " + name, 26, 376 + 16 * i);
    //   i ++;
    // }

    for(Button btn : narrowButtons.values()) {
      btn.draw();
    }
  }

  @Override
  public void tick(float dTime) {
    for(Button btn : narrowButtons.values()) {
      btn.update();
    }
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    this.selected = selected;

    selectedTypes.clear();
    narrowButtons.clear();
    narrowBuckets.clear();
    for(ISelectable selectable : selected) {
      String name = selectable.getClass().getName();
      System.out.println(name);
      String[] splitName = name.split("\\.");
      for(String s : splitName) {
        System.out.println(s);
      }
      String shortName = splitName[splitName.length - 1];
      
      if(selectedTypes.containsKey(name)) {
        selectedTypes.replace(name, selectedTypes.get(name) + 1);
        Button btn = narrowButtons.get(name);
        List<ISelectable> items = narrowBuckets.get(btn);
        items.add(selectable);
      } else {
        Button btn = new Button(Assets.uiFrame, shortName, 20, 376 + 30 * selectedTypes.size(), 280, 24);
        btn.registerClickListener(this);
        selectedTypes.put(name, 1);
        narrowButtons.put(name, btn);
        List<ISelectable> list = new ArrayList<ISelectable>();
        list.add(selectable);
        narrowBuckets.put(btn, list);
      }
    }
  }

  @Override
  public void click(Button target) {
    
  }
}
