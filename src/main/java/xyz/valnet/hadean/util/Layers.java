package xyz.valnet.hadean.util;

public class Layers {
  private static int current = 0;
  public static int getMax() {
    return current;
  }
  
  public static final int BACKGROUND = current ++;
  public static final int TILES = current ++;
  public static final int GROUND = current ++;
  public static final int AIR = current ++;
  public static final int MARKERS = current ++;
  public static final int SELECTION_IDENTIFIERS = current ++;
  public static final int AREA_SELECT_BOX = current ++;
  public static final int GENERAL_UI = current ++;
  public static final int GENERAL_UI_INTERACTABLE = current ++;
  public static final int BOTTOM_BAR = current ++;

}
