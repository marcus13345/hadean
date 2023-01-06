package xyz.valnet.hadean.interfaces;

public interface IBuildLayerListener {
  public void update(int x, int y, int w, int h);
  public void build(int x, int y, int w, int h);
  public void build(int x, int y);
  public void cancel();
}
