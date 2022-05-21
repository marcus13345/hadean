package xyz.valnet.hadean.input;

import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.App;
import xyz.valnet.engine.graphics.Tile9;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.hadean.util.Assets;

public class Button {

  private final int x, y, width, height;
  private String text;
  protected Tile9 frame;
  protected Tile9 frameHover;
  protected Tile9 frameActive;
  private int textWidth, textHeight;
  private float hPad, vPad;
  private Vector4i box;

  protected float hoverVPad = 0.0f;
  protected float hoverHPad = 0.1f;
  protected float activeVPad = 0.1f;
  protected float activeHPad = 0.0f;

  public Button(Tile9 frame, String text, int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    width = w;
    height = h;
    this.frame = frame;
    this.frameActive = frame;
    this.frameHover = frame;
    setText(text);
    box = new Vector4i(x, y, w, h);
  }

  public void setText(String text) {
    this.text = text;
    Vector4i measuredText = Assets.font.measure(text);
    textWidth = measuredText.x;
    textHeight = measuredText.y;
  }

  public void draw() {
    if(state == HOVER) {
      frameHover.draw(box.x, box.y, box.z, box.w);
    } else if(state == ACTIVE) {
      frameActive.draw(box.x, box.y, box.z, box.w);
    } else {
      frame.draw(box.x, box.y, box.z, box.w);
    }

    Assets.flat.pushColor(Vector4f.black);
    Assets.font.drawString(text, 1 + x + (width - textWidth) / 2, 1 + y + (height - textHeight) / 2);

    Assets.flat.swapColor(Vector4f.one);
    Assets.font.drawString(text, x + (width - textWidth) / 2, y + (height - textHeight) / 2);
    
    Assets.flat.popColor();
  }

  private boolean hovered = false;

  private int state = 0;
  private final static int IDLE = 0;
  private final static int INACTIVE = 1;
  private final static int HOVER = 2;
  private final static int ACTIVE = 3;
  private final static int ACTIVE_NO_HOVER = 4;

  private IButtonListener listener = null;

  public void update() {
    box.x = x - (int)hPad;
    box.y = y - (int)vPad;
    box.z = width + ((int)hPad) * 2;
    box.w = height + ((int)vPad) * 2;

    hovered = App.mouseX >= box.x && App.mouseX <= box.x + box.z && App.mouseY >= box.y && App.mouseY <= box.y + box.w;
    boolean mouseDown = App.mouseLeft;

    float desiredVPad = 0, desiredHPad = 0;

    if(state == HOVER) {
      desiredVPad += height * hoverVPad;
      desiredHPad += width * hoverHPad;
    }

    if(state == ACTIVE) {
      desiredVPad += height * activeVPad;
      desiredHPad += width * activeHPad;
    }

    vPad = lerp(vPad, desiredVPad, 0.1f);
    hPad = lerp(hPad, desiredHPad, 0.1f);

    if(state == IDLE) {
      if(hovered) {
        state = HOVER;
      } else if (mouseDown) {
        state = INACTIVE;
      }
    } else if (state == HOVER) {
      if(!hovered) {
        state = IDLE;
      } else if(mouseDown) {
        state = ACTIVE;
      }
    } else if (state == INACTIVE) {
      if(!mouseDown) {
        state = IDLE;
      }
    } else if (state == ACTIVE) {
      if(!hovered) {
        state = ACTIVE_NO_HOVER;
      } else if(!mouseDown) {
        state = IDLE;
        listener.click(this);
      }
    } else if (state == ACTIVE_NO_HOVER) {
      if(hovered) {
        state = ACTIVE;
      } else if (!mouseDown) {
        state = IDLE;
      }
    }

  }

  public void registerClickListener(IButtonListener listener) {
    this.listener = listener;
  }
}
