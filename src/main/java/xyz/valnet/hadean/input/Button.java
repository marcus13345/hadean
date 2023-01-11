package xyz.valnet.hadean.input;

import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Tile9;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.util.Assets;

public class Button extends GameObject implements IMouseCaptureArea, ITransient {

  private int x, y, width, height;
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

  protected float layer;

  public Button(Tile9 frame, String text, int x, int y, int w, int h, float l) {
    this.x = x;
    this.y = y;
    width = w;
    height = h;
    this.frame = frame;
    this.frameActive = frame;
    this.frameHover = frame;
    setText(text);
    box = new Vector4i(x, y, w, h);
    layer = l;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    Vector4i measuredText = Assets.font.measure(text);
    textWidth = measuredText.x;
    textHeight = measuredText.y;
  }

  @Override
  public void render() {
    Drawing.setLayer(layer);
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

  // public void draw(int x, int y, int w, int h) {
  //   this.x = x;
  //   this.y = y;
  //   width = w;
  //   height = h;
  //   render();
  // }

  private boolean hovered = false;
  private boolean mouseDown = false;

  private int state = 0;
  private final static int IDLE = 0;
  private final static int INACTIVE = 1;
  private final static int HOVER = 2;
  private final static int ACTIVE = 3;
  private final static int ACTIVE_NO_HOVER = 4;

  private IButtonListener listener = null;

  public void update() {
    update(1);
  }

  @Override
  public void update(float dTime) {
    box.x = x - (int)hPad;
    box.y = y - (int)vPad;
    box.z = width + ((int)hPad) * 2;
    box.w = height + ((int)vPad) * 2;

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
        Assets.sndGlassTap.play();
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
        if(!hovered) {
          state = IDLE;
        } else {
          state = HOVER;
        }
        listener.click(this);
        Assets.sndBubble.play();
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

  @Override
  public void mouseEnter() {
    hovered = true;
  }

  @Override
  public void mouseLeave() {
    hovered = false;
  }

  @Deprecated 
  // this should only be used when its not added to a scene (with gameobjects!),
  // which increasingly should NOT be the case.
  public void setMouseCoords(float x, float y) {
    hovered = x >= box.x && x <= box.x + box.z && y >= box.y && y <= box.y + box.w;
  }

  @Override
  public void mouseDown(int button) {
    if(button == 0) {
      mouseDown = true;
    }
  }

  @Override
  public void mouseUp(int button) {
    if(button == 0) {
      mouseDown = false;
    }
  }

  @Override
  public Vector4f getBox() {
    return new Vector4f(x, y, width, height);
  }

  @Override
  public float getLayer() {
    return layer;
  }
}
