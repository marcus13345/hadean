package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.scenegraph.ITransient;
import static xyz.valnet.engine.util.Math.lerp;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.interfaces.IBottomBarItem;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public abstract class Tab extends ImmediateUI implements IBottomBarItem, ITransient {

  private BottomBar bottombar;

  protected boolean opened = false;
  private float animation = 0f;

  @Override
  public void update(float dTime) {
    animation = lerp(animation, opened ? 1 : 0, dTime / 20);
  }

  @Override
  protected void connect() {
    bottombar = get(BottomBar.class);
  }
  
  @Override
  protected void start() {
    bottombar.registerButton(this);
  }

  @Override
  public final boolean isButtonClickSilent() {
    return true;
  }

  @Override
  public final float getLayer() {
    return Layers.GENERAL_UI;
  }

  protected final boolean shouldRender() {
    return opened || animation > 0.0001f;
  }

  protected final int animate(float a, float b) {
    return (int)Math.round(lerp(a, b, animation));
  }

  @Override
  public final void evoke() {
    if(opened) close();
    else open();
  }

  public final void open() {
    if(opened) return;
    Assets.sndBubble.play();
    opened = true;
    onOpen();
  }

  public final void close() {
    if(!opened) return;
    Assets.sndCancel.play();
    opened = false;
    onClose();
  }

  protected abstract void onClose();
  protected abstract void onOpen();
}
