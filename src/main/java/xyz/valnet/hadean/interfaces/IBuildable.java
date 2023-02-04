package xyz.valnet.hadean.interfaces;

public interface IBuildable {

  public void buildAt(int x, int y, int w, int h);
  @Deprecated
  public void buildAt(int x, int y);

  public String getBuildTabCategory();
  public BuildType getBuildType();
  public String getBuildTabName();

}
