package xyz.valnet.hadean.interfaces;

public interface IBuildLayerListener {
  @Deprecated
  public void update(int x, int y, int w, int h);
  @Deprecated
  public void build(int x, int y, int w, int h);
  public void cancel();
}
