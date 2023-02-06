package xyz.valnet.hadean.interfaces;

import xyz.valnet.engine.math.Box;
import xyz.valnet.engine.math.Vector2i;

public interface IBuildable {

  public void buildAt(Box box);

  public String getBuildTabCategory();
  public BuildType getBuildType();
  public String getBuildTabName();

  public default Vector2i getDimensions() {
    return null;
  }

}
