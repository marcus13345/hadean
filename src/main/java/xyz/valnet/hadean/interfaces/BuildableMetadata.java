package xyz.valnet.hadean.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BuildableMetadata {
  public String name();
  public String category();
}
