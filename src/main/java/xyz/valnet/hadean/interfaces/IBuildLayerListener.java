package xyz.valnet.hadean.interfaces;

public interface IBuildLayerListener {
  public void update(int x, int y, int w, int h);
  public void select(int x, int y, int w, int h);
  public void cancel();
}
