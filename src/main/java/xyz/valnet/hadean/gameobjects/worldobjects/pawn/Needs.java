package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import java.io.Serializable;

import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.PercentDetail;

public class Needs implements Serializable {

  private float energy = 0.7f + (float)Math.random() * 0.3f;
  private float recreation = 0.5f + (float)Math.random() * 0.5f;

  private float restRatio = 3;
  private float decay = 0.000004f;

  public void update(float dTime) {
    energy = Math.max(energy - decay, 0);
    recreation = Math.max(recreation - decay, 0);
  }

  public void sleep() {
    energy = Math.min(energy + decay * restRatio, 1);
  }

  public float getSleepNeed() {
    return 1 - energy;
  }

  // public
  
  public Detail[] getDetails() {
    return new Detail[] {
      new PercentDetail("Energy", energy, 1),
      new PercentDetail("Fun", recreation, 1)
    };
  }


}
