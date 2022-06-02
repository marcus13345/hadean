package xyz.valnet.hadean.util;

public class Layers {
  private static int current = 0;
  public static int getMax() {
    return current;
  }
  
  public static final float BACKGROUND = current ++;
  public static final float TILES = current ++;
  public static final float GROUND = current ++;
  public static final float AIR = current ++;
  public static final float PAWNS = Layers.AIR + 0.001f;
  public static final float MARKERS = current ++;
  public static final float SELECTION_IDENTIFIERS = current ++;
  public static final float AREA_SELECT_BOX = current ++;
  public static final float BUILD_INTERACTABLE = current ++;
  public static final float GENERAL_UI = current ++;
  public static final float GENERAL_UI_INTERACTABLE = current ++;
  public static final float BOTTOM_BAR = current ++;

}
