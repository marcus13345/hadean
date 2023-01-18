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

  @SuppressWarnings("unused")
  private boolean active;
  @SuppressWarnings("unused")
  private boolean mouseDown;

  public void render() {
    begin();
    gui();
    end();
  }

  public void renderAlpha() {

    float f = 99;
    Assets.flat.pushColor(new Vector4f(1, 0, 0, 0.3f));
    for(Vector4f box : guiAreas) {
      Drawing.setLayer(f += 0.001f);
      Assets.fillColor.draw(box);
    }
    Assets.flat.popColor();
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

  @Override
  public float getLayer() {
    return Layers.GENERAL_UI_INTERACTABLE;
  }

  protected abstract void gui();

  private record StackingContext(
    boolean fixedSize,
    Vector4f box,
    Vector4f occlusionBox,
    boolean hasRegisteredGuiArea
    // layout manager?
  ) {}

  private transient StackingContext context;
  private transient Stack<StackingContext> contextStack = new Stack<StackingContext>();;

  private transient Map<String, Button> buttons = new HashMap<String, Button>();
  private transient Set<String> usedButtonId = new HashSet<String>();
  private transient Map<Button, Integer> clicks = new HashMap<Button, Integer>();
  private transient int buttonCount;

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

  @Override
  public final List<Vector4f> getGuiBoxes() {
    return guiAreas;
  }

  private transient List<Vector4f> guiAreas = new ArrayList<Vector4f>();

  // === ELEMENTS ===
  // 

  protected void begin() {
    buttonCount = 0;
    usedButtonId.clear();
    contextStack.clear();
    guiAreas.clear();
  }

  protected void root(int x, int y, int w, int h) {
    assert context == null : "root can only be a root element";
    Vector4f box = new Vector4f(x, y, w, h);
    context = new StackingContext(true, box, box, false);
  }

  protected void rootEnd() {
    assert contextStack.size() == 0 : "cannot end fixedFrame with un-ended elements inside";
    context = null;
  }

  protected void fixedFrame(int w, int h) {
    contextStack.push(context);
    context = new StackingContext(
      true,
      new Vector4f(context.box.x, context.box.y, w, h),
      context.occlusionBox.copy(),
      true
    );
  }

  protected void dynamicFrame() {
    contextStack.push(context);
    context = new StackingContext(
      false,
      new Vector4f(context.box.x, context.box.y, context.box.z, 0),
      context.occlusionBox.copy(),
      true
    );
  }

  // TODO this will add _all_ frames, not just root frames to guiareas.
  // not a problem, but not efficient. revisit.
  protected void frameEnd() {
    Drawing.setLayer(getLayer() + contextStack.size() - 1);
    if(!context.fixedSize) {
      Assets.uiFrame.draw(context.box);
      guiAreas.add(context.box);
    } else {
      Assets.uiFrame.draw(context.occlusionBox);
      guiAreas.add(context.occlusionBox);
    }
    context = contextStack.pop();
  }

  protected boolean button(String text) {
    return button(genButtonId(), text, false);
  }

  protected boolean button(String text, boolean expand) {
    return button(genButtonId(), text, expand);
  }

  protected boolean button(String id, String text) {
    return button(id, text, false);
  }

  protected boolean button(String id, String text, boolean expand) {
    float h = 32;
    if(expand && context.fixedSize) {
      h = context.box.w;
    }
    Vector4f buttonBox = new Vector4f(context.box.x, context.box.y, context.box.z, h);
    Button btn = getButton(id);

    if(!context.hasRegisteredGuiArea) {
      guiAreas.add(buttonBox);
    }

    btn.setText(text);
    btn.setPosition((int) buttonBox.x, (int) buttonBox.y);
    btn.setSize((int) buttonBox.z, (int) buttonBox.w);
    btn.setLayer(getLayer() + contextStack.size() + 1);

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
      context.occlusionBox.copy(),
      context.hasRegisteredGuiArea
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
        context.occlusionBox.copy(),
        context.hasRegisteredGuiArea
      );
    } else {
      context = new StackingContext(false,
        new Vector4f(
          context.box.x + 8, context.box.y + 8, context.box.z - 16, 0
        ),
        context.occlusionBox.copy(),
        context.hasRegisteredGuiArea
      );
    }
  }

  protected void padEnd() {
    float h = context.box.w + 16;
    context = contextStack.pop();
    adjustBox(h);
  }

  protected void end() {

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
