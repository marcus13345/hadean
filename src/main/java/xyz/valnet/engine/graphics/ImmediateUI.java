package xyz.valnet.engine.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public abstract class ImmediateUI extends GameObject implements IMouseCaptureArea {

  private boolean active;
  private boolean mouseDown;

  public void render() {
    begin();
    gui();
    end();
  }

  @Override
  public void mouseEnter() {
    active = true;
  }

  @Override
  public void mouseLeave() {
    active = false;
  }

  @Override
  public void mouseDown(int button) {
    if(button == 0) mouseDown = true;
  }

  @Override
  public void mouseUp(int button) {
    if(button == 0) mouseDown = false;
  }

  public abstract Vector4f getGuiBox();

  @Override
  public float getLayer() {
    return Layers.GENERAL_UI_INTERACTABLE;
  }

  protected abstract void gui();

  private record StackingContext(
    boolean fixedSize,
    Vector4f box,
    Vector4f occlusionBox
    // layout manager?
  ) {}

  private StackingContext context;
  private Stack<StackingContext> contextStack;

  private transient Map<String, Button> buttons = new HashMap<String, Button>();
  private Set<String> usedButtonId = new HashSet<String>();
  private transient Map<Button, Integer> clicks = new HashMap<Button, Integer>();
  private int buttonCount;

  private String genButtonId() {
    buttonCount ++;
    return "DefaultButton-" + buttonCount;
  }

  private boolean hasButton(String id) {
    usedButtonId.add(id);
    return buttons.containsKey(id);
  }

  private Button getButton(String id) {
    if(hasButton(id)) return buttons.get(id);
    System.out.println("Created new Button");
    Button btn = new Button(Assets.uiFrame, "", 0, 0, 0, 0, 0);
    btn.registerClickListener((target) -> {
      if(!clicks.containsKey(target)) clicks.put(target, 0);
      clicks.put(target, clicks.get(target) + 1);
    });
    buttons.put(id, add(btn));
    return btn;
  }

  private boolean getClick(Button btn) {
    if(clicks.containsKey(btn)) {
      int clickCount = clicks.get(btn);
      if(clickCount > 0) {
        clicks.put(btn, clickCount - 1);
        return true;
      }
    }
    return false;
  }

  private void adjustBox(float h) {
    if(context.fixedSize) {
      context.box.y += h;
      context.box.w -= h;
    } else {
      context.box.w += h;
    }
  }

  // === ELEMENTS ===

  protected void begin() {
    buttonCount = 0;
    usedButtonId.clear();
    Drawing.setLayer(Layers.GENERAL_UI_INTERACTABLE);
    context = new StackingContext(true, getGuiBox(), getGuiBox());
    contextStack = new Stack<StackingContext>();
    Assets.uiFrame.draw(context.occlusionBox);
    pad();
  }

  protected boolean button(String text) {
    return button(genButtonId(), text);
  }

  protected boolean button(String id, String text) {
    float h = 32;
    Vector4f buttonBox = new Vector4f(context.box.x, context.box.y, context.box.z, h);
    Button btn = getButton(id);

    btn.setText(text);
    btn.setPosition((int) buttonBox.x, (int) buttonBox.y);
    btn.setSize((int) buttonBox.z, (int) buttonBox.w);
    btn.setLayer(Layers.GENERAL_UI_INTERACTABLE + contextStack.size());

    // System.out.println("" + buttonBox);

    adjustBox(h);

    return getClick(btn);
  }

  protected void horizontal(int columns) {
    
  }

  protected void horizontalEnd() {

  }

  protected void group() {
    contextStack.push(context);
    context = new StackingContext(false,
      new Vector4f(
        context.box.x, context.box.y, context.box.z, 0
      ),
      context.occlusionBox.copy()
    );
    pad();
  }

  protected void groupEnd() {
    padEnd();
    Drawing.setLayer(getLayer() + contextStack.size() - 1);
    float h = context.box.w;
    Assets.uiFrame.draw(context.box);
    context = contextStack.pop();
    adjustBox(h);
  }

  protected void pad() {
    contextStack.push(context);
    if(context.fixedSize) {
      context = new StackingContext(true,
        new Vector4f(
          context.box.x + 8, context.box.y + 8, context.box.z - 16, context.box.w - 16
        ),
        context.occlusionBox.copy()
      );
    } else {
      context = new StackingContext(false,
        new Vector4f(
          context.box.x + 8, context.box.y + 8, context.box.z - 16, 0
        ),
        context.occlusionBox.copy()
      );
    }
  }

  protected void padEnd() {
    float h = context.box.w + 16;
    context = contextStack.pop();
    adjustBox(h);
  }

  protected void end() {
    padEnd();

    List<String> buttonIdsToRemove = new ArrayList<String>();

    for(String id : this.buttons.keySet()) {
      if(usedButtonId.contains(id)) continue;
      buttonIdsToRemove.add(id);
    }

    for(String id : buttonIdsToRemove) {
      Button btn = buttons.get(id);
      remove(btn);
      buttons.remove(id);
      if(clicks.containsKey(btn)) {
        clicks.remove(btn);
      }
    }
  }

  protected void text(String text) {
    text(text, Assets.font);
  }

  protected void header(String text) {
    text(text, Assets.bigFont);
  }

  protected void text(String text, Font font) {
    Vector4i measured = font.measure(text);
    Drawing.setLayer(getLayer() + contextStack.size());

    // layout manager things...
    if(context.fixedSize) {
      font.drawString(text,
        (int) context.box.x,
        (int) context.box.y
      );
      context.box.y += measured.y;
      context.box.w -= measured.y;
    } else {
      font.drawString(text,
        (int) context.box.x,
        (int) context.box.y + (int) context.box.w
      );
      context.box.w += measured.y;
    }
  }
}
