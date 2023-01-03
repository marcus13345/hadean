package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.util.Math.WeightedAverage;
import xyz.valnet.hadean.gameobjects.Clock;
import xyz.valnet.hadean.gameobjects.worldobjects.agents.Agent;

import static xyz.valnet.engine.util.Math.lerp;

public class SleepActivity extends Activity {
  
  private Agent agent;
  private Needs needs;
  private Clock clock;

  private float circadianStrength = (float)Math.random() * 5f;

  private int stage;

  public SleepActivity(Agent agent, Needs needs, Clock clock) {
    this.needs = needs;
    this.agent = agent;
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
    // System.out.println(1 - 2 * clock.getSunlight());
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
