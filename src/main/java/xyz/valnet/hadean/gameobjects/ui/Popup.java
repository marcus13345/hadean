package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.math.Vector4f;

public class Popup extends ImmediateUI {

  @Override
  public Vector4f getGuiBox() {
    return new Vector4f(256, 100, 512, 300);
  }

  @Override
  protected void gui() {
    header(" Popup Test");

    text("1\n1.5");
    text("2");

    group();
      text("This should be in a frame!");
      text("And this!");
    groupEnd();
    
    text("But not this...");

    if(button("Click Me!")) {
      System.out.println("The Event!");
    }

    text("This after button...");
  }
  
}
