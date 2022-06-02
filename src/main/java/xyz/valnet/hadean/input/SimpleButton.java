package xyz.valnet.hadean.input;

import xyz.valnet.hadean.util.Assets;

public class SimpleButton extends Button {

  public SimpleButton(String text, int x, int y, int w, int h, float l) {
    super(Assets.uiFrame, text, x, y, w, h, l);

    this.activeHPad = 0f;
    this.activeVPad = 0f;
    this.hoverHPad = 0f;
    this.hoverVPad = 0f;

    this.frame = Assets.uiFrame;
    this.frameHover = Assets.uiFrameLight;
    this.frameActive = Assets.uiFrameDark;
    
  }
  
}
