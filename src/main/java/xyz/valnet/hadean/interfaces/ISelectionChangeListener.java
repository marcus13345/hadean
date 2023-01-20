package xyz.valnet.hadean.interfaces;

import java.io.Serializable;
import java.util.List;

@FunctionalInterface
public interface ISelectionChangeListener extends Serializable {
  public void selectionChanged(List<ISelectable> selected);
}
