package xyz.valnet.hadean.designation;

import java.util.List;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.BuildType;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;

public abstract class Designation<T extends ISelectable> extends GameObject implements IBuildable {

  @Override
  @SuppressWarnings("unchecked")
  public void buildAt(int x, int y, int w, int h) {
    Class<T> type = getType();
    List<T> things = getAll(type);
    for(ISelectable thing : things) {
      Vector4f box = thing.getWorldBox();
      if(rectanglesIntersect(x, y, x + w, y + h, box.x, box.y, box.z, box.w))
      designate((T) thing);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void buildAt(int x, int y) {
    Class<T> type = getType();
    List<T> things = getAll(type);
    for(ISelectable thing : things) {
      Vector4f box = thing.getWorldBox();
      if(rectanglesIntersect(x, y, x + 1, y + 1, box.x, box.y, box.z, box.w))
      designate((T) thing);
    }
  }

  public boolean rectanglesIntersect( 
    float minAx, float minAy, float maxAx, float maxAy,
    float minBx, float minBy, float maxBx, float maxBy ) {
    boolean aLeftOfB = maxAx <= minBx;
    boolean aRightOfB = minAx >= maxBx;
    boolean aAboveB = minAy >= maxBy;
    boolean aBelowB = maxAy <= minBy;

    return !( aLeftOfB || aRightOfB || aAboveB || aBelowB );
  }

  @Override
  public String getBuildTabCategory() {
    return "Jobs";
  }

  @Override
  public BuildType getBuildType() {
    return BuildType.AREA;
  }
  
  protected abstract Class<T> getType();
  protected abstract void designate(T thing);
}
