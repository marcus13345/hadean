package xyz.valnet.hadean.gameobjects.worldobjects.items;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.util.SmartBoolean;

public class Item extends WorldObject {
  protected JobBoard jobboard;

  private SmartBoolean haul;

  // camera.draw(Layers.MARKERS, Assets.haulArrow, x, y);

  @Override
  public void start() {
    super.start();
    haul = new SmartBoolean(false, new SmartBoolean.IListener() {
      @Override
      public void rise() {
        
      }
      
      @Override
      public void fall() {
        
      }
    });
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Vector4f getWorldBox() {
    return null;
  }
}
