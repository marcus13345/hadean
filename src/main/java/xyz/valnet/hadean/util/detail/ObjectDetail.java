package xyz.valnet.hadean.util.detail;

public class ObjectDetail<T> extends Detail {
  private T value;

  public ObjectDetail(String key, T value) {
    this.key = key;
    this.value = value;
  }

  public String toString(int keyWidth) {
    String prefix = key + " ".repeat(keyWidth - getKeyWidth()) + " | ";
    if(value == null)
      return prefix + "null";
    return prefix + value.toString();
  }
}