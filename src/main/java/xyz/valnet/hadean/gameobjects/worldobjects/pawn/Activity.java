package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import java.io.Serializable;

import xyz.valnet.engine.math.Vector2i;

public abstract class Activity implements Serializable {

  @FunctionalInterface
  public interface ActivityCancellationCallback extends Serializable {
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
