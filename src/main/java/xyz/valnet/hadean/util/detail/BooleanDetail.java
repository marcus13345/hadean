package xyz.valnet.hadean.util.detail;

public class BooleanDetail extends Detail {
  private boolean value;

  public BooleanDetail(String key, boolean value) {
    this.key = key;
    this.value = value;
  }

  public String toString(int keyWidth) {
    return key + " ".repeat(keyWidth - getKeyWidth()) + " | " + java.lang.Boolean.toString(value);
  }
}