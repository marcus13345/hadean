package xyz.valnet.hadean.gameobjects;

import java.util.List;

public interface ISelectionChangeListener {
  public void selectionChanged(List<ISelectable> selected);
}
