package xyz.valnet.hadean.gameobjects.worldobjects.agents.pawn;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.terrain.Terrain;
import xyz.valnet.hadean.gameobjects.terrain.Tile;

// TODO actually implement this activity.
public class WanderActivity extends Activity {

  // TODO implement fun?
  // private Needs needs;

  private Terrain terrain;

  public WanderActivity(Terrain terrain) {
    this.terrain = terrain;
  }

  @Override
  public boolean isUrgent() {
    return false;
  }

  @Override
  public float getBenefit() {
    return 0.0f;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public void act(float dTime) {
    callback.apply(this);
  }

  ActivityCancellationCallback callback;

  @Override
  public void begin(ActivityCancellationCallback callback) {
    this.callback = callback;

    Tile tile = terrain.getRandomWalkableTile();
    if(tile == null) {
      callback.apply(this);
      return;
    }
    target = tile.getCoords();
  }

  @Override
  public void end() { }

  @Override
  public String toString() {
    return "Wandering";
  }

  private Vector2i target = null;

  @Override
  public Vector2i[] getTargetLocations() {
    if(target == null) return null;
    return new Vector2i[] { target };
  }
  
}
