package xyz.valnet.hadean.gameobjects.ui.tabs;

import static xyz.valnet.engine.util.Math.lerp;

import xyz.valnet.engine.graphics.IModalUI;
import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.Constants;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.ui.ExclusivityManager;
import xyz.valnet.hadean.interfaces.IBottomBarItem;
import xyz.valnet.hadean.util.Layers;

public abstract class Tab extends ImmediateUI implements IBottomBarItem, ITransient, IModalUI {

  private BottomBar bottombar;

  protected boolean opened = false;
  private float animation = 0f;

  private ExclusivityManager exclusivityManager;

  @Override
  public void fixedUpdate(float dTime) {
    animation = lerp(animation, opened ? 1 : 0, dTime / Constants.animationSpeed);
  }

  @Override
  protected void connect() {
    bottombar = get(BottomBar.class);
    exclusivityManager = get(ExclusivityManager.class);
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
    return (int) Math.round(lerp(a, b, animation));
  }

  @Override
  public final void evoke() {
    if(opened) close();
    else open();
  }

  @Override
  public final void open() {
    if(opened) return;
    opened = true;
    exclusivityManager.switchTo(this);
    onOpen();
  }

  @Override
  public final void close() {
    if(!opened) return;
    opened = false;
    exclusivityManager.closeCurrent();
    onClose();
  }

  protected abstract void onClose();
  protected abstract void onOpen();
}
