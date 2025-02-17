package xyz.valnet.hadean.gameobjects.worldobjects.agents.pawn;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.util.Math.WeightedAverage;
import xyz.valnet.hadean.gameobjects.Clock;

public class SleepActivity extends Activity {
  
  private Needs needs;
  private Clock clock;

  private float circadianStrength = (float)Math.random() * 5f;


  public SleepActivity(Needs needs, Clock clock) {
    this.needs = needs;
    this.clock = clock;
  }

  @Override
  public boolean isUrgent() {
    return needs.getSleepNeed() >= 0.97f;
  }

  @Override
  public float getBenefit() {
    // subtract because sleeping for only 5 minutes when
    // you're not that tired to hit 100% is undesireable.
    // as it will induce oversleep
    WeightedAverage average = new WeightedAverage();
    average.add(needs.getSleepNeed(), 1);
    
    average.add(1 - 2 * clock.getSunlight(), circadianStrength);
    return average.calculate();
  }

  // isValid vs canBeStarted? idk, maybe thats not important.
  @Override
  public boolean isValid() {
    return needs.getSleepNeed() > 0.2f;
  }

  @Override
  public void act(float dTime) {
    needs.sleep(dTime);
    if(needs.getSleepNeed() == 0) {
      callback.apply(this);
    }
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
