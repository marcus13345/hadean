package xyz.valnet.hadean.gameobjects;

public interface ITileThing {
  public boolean isWalkable();
  public boolean shouldRemove();
  public void onRemove();
}
