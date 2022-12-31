package xyz.valnet.hadean.util.detail;

public class PercentDetail extends Detail {
  private float value;
  private int sigFigs;

  public PercentDetail(String key, float value) {
    this(key, value, 0);
  }

  public PercentDetail(String key, float value, int sigFigs) {
    this.key = key;
    this.value = value;
    this.sigFigs = sigFigs;
  }

  public String toString(int keyWidth) {
    double sigFigMul = Math.pow(10, sigFigs);
    return key + " ".repeat(keyWidth - getKeyWidth()) + " | " + ((Math.round(value * (sigFigMul * 100))) / sigFigMul) + "%";
  }
}