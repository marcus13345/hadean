package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.Layers;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.input.IButtonListener;
import xyz.valnet.hadean.input.IMouseListener;
import xyz.valnet.hadean.input.SimpleButton;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class SelectionUI extends GameObject implements ISelectionChangeListener, IButtonListener, IMouseListener {

  private String name = "";
  private int count = 0;
  private List<ISelectable> selected = new ArrayList<ISelectable>();
  private HashMap<String, Integer> selectedTypes = new HashMap<String, Integer>();
  private HashMap<String, Button> narrowButtons = new HashMap<String, Button>();
  private HashMap<Button, List<ISelectable>> narrowBuckets = new HashMap<Button, List<ISelectable>>();

  private static final Button[] ACTIONS_BUTTONS_NULL = new Button[] {};
 
  private Button[] actionButtons = ACTIONS_BUTTONS_NULL;

  private Selection selectionManager;
  private final int width = 300, height = 200;
  private final int padding = 10;
  private final int actionButtonSize = 100;

  // this will be null normally, but set if
  // a button has been pressed to update the selection.
  // its a simple workaround to get rid of a concurrent
  // exception, where the buttons are attempting to
  // change while updating. 
  // TODO this could be fixed by delaying button clicks to the next frame.
  private List<ISelectable> newSelection = null;;

  public void start() {
    selectionManager = get(Selection.class);
    selectionManager.subscribe(this);
  }

  public void render() {
    if(selected.isEmpty()) return;

    Assets.uiFrame.draw(10, 576 - BottomBar.bottomBarHeight - height - padding, width, height);

    // int i = 0;
    // for(String name : selectedTypes.keySet()) {
    //   int n = selectedTypes.get(name);
    //   Assets.font.drawString("" + n + "x " + name, 26, 376 + 16 * i);
    //   i ++;
    // }


    if(selectedTypes.size() == 1) {
      Assets.font.drawString("" + count + "x " + name, 26, 576 - BottomBar.bottomBarHeight - height);

      if(count == 1) {
        String details = selected.get(0).details();
        Assets.font.drawString(details, 26, 576 - BottomBar.bottomBarHeight - height + 32);
      }

    } else {
    }

  }

  @Override
  public void update(float dTime) {
    if(newSelection != null) {
      selectionManager.updateSelection(newSelection);
      newSelection = null;
    }

    if(selectedTypes.size() == 1) {
    } else {
    }
  }

  private HashMap<Button, Action> buttonActionMap = new HashMap<Button, Action>();

  private void addNarrowButton(String str, Button btn) {
    narrowButtons.put(str, btn);
    add(btn);
  }

  private void clearNarrowButtons() {
    for(GameObject obj : narrowButtons.values()) {
      remove(obj);
    }
    narrowButtons.clear();
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    this.selected = selected;

    selectedTypes.clear();
    clearNarrowButtons();
    narrowBuckets.clear();
    buttonActionMap.clear();
    setActionButtons(ACTIONS_BUTTONS_NULL);
    for(ISelectable selectable : selected) {
      String name = selectable.getClass().getName();
      String[] splitName = name.split("\\.");
      String shortName = splitName[splitName.length - 1];
      
      if(selectedTypes.containsKey(name)) {
        selectedTypes.replace(name, selectedTypes.get(name) + 1);
        Button btn = narrowButtons.get(name);
        List<ISelectable> items = narrowBuckets.get(btn);
        items.add(selectable);
        btn.setText("" + items.size() + "x " + shortName);
        count ++;
      } else {
        Button btn = new SimpleButton("1x " + shortName, 20, 576 - BottomBar.bottomBarHeight - height + 30 * selectedTypes.size(), width - padding * 2, 24, Layers.GENERAL_UI_INTERACTABLE);
        btn.registerClickListener(this);
        selectedTypes.put(name, 1);
        addNarrowButton(name, btn);
        List<ISelectable> list = new ArrayList<ISelectable>();
        list.add(selectable);
        narrowBuckets.put(btn, list);
        count = 1;
        this.name = shortName;
      }
    }
    if(selectedTypes.size() == 1) {
      // TODO this should only pull common actions to all elements, but rn just pulls the first things actions
      Action[] actions = selected.get(0).getActions();
      Button[] actionButtons = new Button[actions.length];
      for(int i = 0; i < actions.length; i ++) {
        actionButtons[i] = new SimpleButton(actions[i].name, width + padding * 2 + i * (actionButtonSize + padding), 576 - padding - actionButtonSize - BottomBar.bottomBarHeight, actionButtonSize, actionButtonSize, Layers.GENERAL_UI_INTERACTABLE);
        actionButtons[i].registerClickListener(this);
        buttonActionMap.put(actionButtons[i], actions[i]);
      }
      setActionButtons(actionButtons);
    }
    if(selectedTypes.size() <= 1) {
      clearNarrowButtons();
    }
  }

  private void setActionButtons(Button[] buttons) {
    for(Button btn : this.actionButtons) {
      remove(btn);
    }
    this.actionButtons = buttons;
    for(Button btn : this.actionButtons) {
      add(btn);
    }
  }

  @Override
  public void click(Button target) {
    if(narrowBuckets.containsKey(target)) {
      newSelection = narrowBuckets.get(target);
    } else if(buttonActionMap.containsKey(target)) {
      Action action = buttonActionMap.get(target);
      for(ISelectable selectable : selected) {
        selectable.runAction(action);
      }
    }
  }

  @Override
  public void mouseEnter() {
    
  }

  @Override
  public void mouseLeave() {
    
  }

  @Override
  public boolean mouseDown(int button) {
    return false;
  }

  @Override
  public void mouseUp(int button) {
    
  }

  @Override
  public Vector4f getBox() {
    if(selected.isEmpty()) return Vector4f.zero;
    return new Vector4f(10, 576 - BottomBar.bottomBarHeight - height - padding, width, height);
  }

  @Override
  public int getLayer() {
    return Layers.GENERAL_UI;
  }
}
