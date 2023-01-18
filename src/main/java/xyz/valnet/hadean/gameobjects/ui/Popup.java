package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.graphics.ImmediateUI;

public class Popup extends ImmediateUI {

  @Override
  protected void gui() {

    window(10, 100, 1004, 200, () -> {
      header(" Popup Test");
  
      horizontal(() -> {
        text("LEFT");
        space(32);
        text("RIGHT");
      });
  
      group(() -> {
        text("This should be in a frame!");
        text("And this!");
      });
      
      text("But not this...");
  
      if(button("Click Me!")) {
        System.out.println("The Event!");
      }
  
      text("This after button...");
    });

    root(10, 350, 1004, 150, () -> {
      horizontal(() -> {
        button("hey");
        space(8);
        button("hello");
      });
    });
  }
  
}
