package xyz.valnet.hadean.interfaces;

import java.util.List;

public interface ISelectionChangeListener {
  public void selectionChanged(List<ISelectable> selected);
}
