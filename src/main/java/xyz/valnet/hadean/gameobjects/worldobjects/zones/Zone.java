package xyz.valnet.hadean.gameobjects.worldobjects.zones;

import xyz.valnet.hadean.gameobjects.worldobjects.Buildable;
import xyz.valnet.hadean.interfaces.BuildType;

public abstract class Zone extends Buildable {

  @Override
  public String getBuildTabCategory() {
    return "Zones";
  }

  @Override
  public BuildType getBuildType() {
    return BuildType.AREA;
  }

  @Override
  public boolean isWalkable() {
    return true;
  }
  
}
