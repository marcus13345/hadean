package xyz.valnet.hadean.util.detail;

public abstract class Detail {

  public abstract String toString(int keyWidth);
  
  protected String key;

  public int getKeyWidth() {
    return key.length();
  }

  public static String renderDetails(Detail[] details) {
    if(details.length == 0) return "";
    int keyWidth = 0;
    for(Detail d : details) {
      keyWidth = Math.max(d.getKeyWidth(), keyWidth);
    }
    
    String str = "";
    for(Detail d : details) {
      str += d.toString(keyWidth) + "\n";
    }
    return str.stripTrailing();
  }

  public static Detail[] mergeDetails(Detail[] a, Detail[] b) {
    Detail[] c = new Detail[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }
}
