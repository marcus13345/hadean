package xyz.valnet.hadean.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BuildableMetadata {

  public static int AREA = 0;
  public static int SINGLE = 1;

  public String name();
  public String category();
  public int type() default AREA;
}
