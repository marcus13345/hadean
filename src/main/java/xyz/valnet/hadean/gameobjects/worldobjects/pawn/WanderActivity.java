package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.worldobjects.agents.Agent;

// TODO actually implement this activity.
public class WanderActivity extends Activity {
  
  @SuppressWarnings("unused")
  private Agent agent;
  @SuppressWarnings("unused")
  private Needs needs;

  public WanderActivity(Agent agent, Needs needs) {
    this.needs = needs;
    this.agent = agent;
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
    // since wandering is literally just pathing.
  }

  ActivityCancellationCallback callback;

  @Override
  public void begin(ActivityCancellationCallback callback) {
    this.callback = callback;
  }

  @Override
  public void end() { }

  @Override
  public String toString() {
    return "Sleeping";
  }

  @Override
  public Vector2i[] getTargetLocations() {
    return null;
  }
  
}
