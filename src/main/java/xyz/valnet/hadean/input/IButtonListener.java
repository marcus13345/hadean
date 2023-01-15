package xyz.valnet.hadean.input;

import java.io.Serializable;

@FunctionalInterface
public interface IButtonListener extends Serializable {
  public void click(Button target);
}