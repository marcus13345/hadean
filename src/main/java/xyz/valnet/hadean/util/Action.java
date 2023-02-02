package xyz.valnet.hadean.util;

public class Action {
  public final String name;

  public Action(String name) {
    this.name = name;
  }

  public static Action[] mergeActions(Action[] a, Action[] b) {
    Action[] c = new Action[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }

  public static Action[] mergeActions(Action[] a, Action[] b, Action[] c) {
    Action[] d = new Action[a.length + b.length + c.length];
    System.arraycopy(a, 0, d, 0, a.length);
    System.arraycopy(b, 0, d, a.length, b.length);
    System.arraycopy(c, 0, d, a.length + b.length, c.length);
    return c;
  }

  public static Action[] mergeActions(Action[] ... args) {
    if(args.length == 1) return new Action[0];
    if(args.length == 1) return args[0];
    if(args.length == 2) return mergeActions(args[0], args[1]);
    if(args.length == 3) return mergeActions(args[0], args[1], args[2]);

    Action[][] merge = new Action[3][];
    Action[][] rest = new Action[args.length - 3][];
    System.arraycopy(args, 0, merge, 0, 3);
    System.arraycopy(args, 3, rest, 0, rest.length);
    
    Action[] first = mergeActions(merge);
    Action[] merged = mergeActions(rest);
    return mergeActions(first, merged);
  }
}
