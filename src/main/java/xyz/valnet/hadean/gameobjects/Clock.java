package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.Game;
import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.HadeanGame;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.PercentDetail;

public class Clock extends GameObject {

  private float time = 7;

  @Override
  public void start() {

  }

  @Override
  public void update(float dTime) {
    time += 0.0004f;
    while (time >= 24) time -= 24;
  }

  public String toString() {
    int hour = (int) Math.floor(time);
    int minutes = (int) Math.floor((time % 1) * 60);
    String hs = hour < 10 ? " " + hour : "" + hour;
    String ms = minutes < 10 ? "0" + minutes : "" + minutes;
    return "" + hs + ":" + ms;
  }

  @Override
  public void render() {
    Drawing.setLayer(Layers.GENERAL_UI);
    String str = toString() + " (Light: " + Math.round(getSunlight() * 100) + "%)";
    int left = 440;
    Assets.flat.pushColor(Vector4f.black);
    Assets.font.drawString(str, left - 1, 9);
    Assets.font.drawString(str, left, 9);
    Assets.font.drawString(str, left + 1, 9);
    Assets.font.drawString(str, left - 1, 10);
    Assets.font.drawString(str, left + 1, 10);
    Assets.font.drawString(str, left - 1, 11);
    Assets.font.drawString(str, left, 11);
    Assets.font.drawString(str, left + 1, 11);
    Assets.flat.swapColor(Vector4f.one);
    Assets.font.drawString(str, left, 10);
    Assets.flat.popColor();
  }

  public float getSunlight() {
    float k = 2;
    float w = 1;
    float u = (k * (float) Math.sin((Math.PI*(time - 7))/(12))) + w;
    double kp = Math.atan(k);
    float m0 = (float)((Math.atan(k + w)) / kp);
    float m1 = (float)((Math.atan(k - w)) / kp);
    float t = (float)((Math.atan(u)) / kp);
    return (m1 + t) / (m1 + m0);
  }
}
