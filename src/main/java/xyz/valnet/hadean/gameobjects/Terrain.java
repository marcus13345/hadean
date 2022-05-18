package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IScene;
import xyz.valnet.hadean.util.Assets;

public class Terrain extends GameObject {

  private final int WORLD_SIZE = 30;
  private final int TILE_SIZE = 8;

  private int[][] variation = new int[WORLD_SIZE][WORLD_SIZE];
  private Vector4f[][] colorVariations = new Vector4f[WORLD_SIZE][WORLD_SIZE];

  public Terrain(IScene scene) {
    super(scene);
    for (int i = 0; i < WORLD_SIZE; i++) {
      for (int j = 0; j < WORLD_SIZE; j++) {
        variation[i][j] = (int) Math.floor(Math.random() * 4);
        colorVariations[i][j] = new Vector4f((float) Math.random() * 0.2f, 0.4f + (float) Math.random() * 0.2f, (float) Math.random() * 0.05f, 1f);
      }
    }
  }

  @Override
  public void render() {
    int left = 400 - (WORLD_SIZE * TILE_SIZE / 2);
    int top = 225 - (WORLD_SIZE * TILE_SIZE / 2);


    Sprite s;
    Assets.flat.pushColor(Vector4f.one);
    for (int i = 0; i < WORLD_SIZE; i++) {
      for (int j = 0; j < WORLD_SIZE; j++) {
        s = Assets.defaultTerrain[variation[i][j]];
        Assets.flat.swapColor(colorVariations[i][j]);
        Drawing.drawSprite(s, left + i * s.width, top + j * s.height);
      }
    }
    Assets.flat.popColor();

  }

}
