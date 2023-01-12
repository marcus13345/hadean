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

  public static Detail[] mergeDetails(Detail[] a, Detail[] b, Detail[] c) {
    Detail[] d = new Detail[a.length + b.length + c.length];
    System.arraycopy(a, 0, d, 0, a.length);
    System.arraycopy(b, 0, d, a.length, b.length);
    System.arraycopy(c, 0, d, a.length + b.length, c.length);
    return c;
  }

  public static Detail[] mergeDetails(Detail[] ... args) {
    if(args.length == 1) return new Detail[0];
    if(args.length == 1) return args[0];
    if(args.length == 2) return mergeDetails(args[0], args[1]);
    if(args.length == 3) return mergeDetails(args[0], args[1], args[2]);

    Detail[][] merge = new Detail[3][];
    Detail[][] rest = new Detail[args.length - 3][];
    System.arraycopy(args, 0, merge, 0, 3);
    System.arraycopy(args, 3, rest, 0, rest.length);
    
    Detail[] first = mergeDetails(merge);
    Detail[] merged = mergeDetails(rest);
    return mergeDetails(first, merged);
  }
}
