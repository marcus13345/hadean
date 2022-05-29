package xyz.valnet.hadean.interfaces;

public interface ITileThing {
  public boolean isWalkable();
  public boolean shouldRemove();
  public void onRemove();
  public void updatePosition(int x, int y);
}
