package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import xyz.valnet.engine.math.Vector2i;

public abstract class Activity {

  @FunctionalInterface
  public interface ActivityCancellationCallback {
    public void apply(Activity activity);
  }

  public abstract boolean isUrgent();
  public abstract float getBenefit();
  public abstract boolean isValid();
  public abstract void act();
  public abstract void begin(ActivityCancellationCallback callback);
  public abstract void end();
  public abstract String toString();
  public abstract Vector2i[] getTargetLocations();
}
