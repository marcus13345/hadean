package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.graphics.ImmediateUI;

public class Popup extends ImmediateUI {

  @Override
  protected void gui() {
    root(100, 100, 200, 200); {
      fixedFrame(200, 100); {
        pad(); {
          header(" Popup Test");
    
          text("1\n1.5");
          text("2");
    
          group(); {
            text("This should be in a frame!");
            text("And this!");
          } groupEnd();
          
          text("But not this...");
    
          if(button("Click Me!")) {
            System.out.println("The Event!");
          }
    
          text("This after button...");
        } padEnd();
      } frameEnd();
    } rootEnd();



    root(724, 100, 200, 200);
      // text("Test Frame!");
      button("Test Button Expand!", true);
      button("Test Button Expand 2!", true);
    rootEnd();
  }
  
}
