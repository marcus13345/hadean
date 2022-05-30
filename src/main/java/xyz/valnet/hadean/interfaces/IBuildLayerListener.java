package xyz.valnet.hadean.interfaces;

public interface IBuildLayerListener {
  public void update(float x, float y, float w, float h);
  public void select(float x, float y, float w, float h);
  public void cancel();
}
