package xyz.valnet.hadean.designation;

import java.util.List;

import xyz.valnet.engine.math.Box;
import xyz.valnet.engine.math.TileBox;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.BuildType;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;

public abstract class Designation<T extends ISelectable> extends GameObject implements IBuildable {

  @Override
  @SuppressWarnings("unchecked")
  public void buildAt(TileBox tileBox) {
    Box box = tileBox.asBox();
    Class<T> type = getType();
    List<T> things = getAll(type);
    for(ISelectable thing : things) {
      Box thingBox = thing.getWorldBox();
      if(box.intersects(thingBox))
        designate((T) thing);
    }
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
