package xyz.valnet.engine.util;

public class Math {
  public static float lerp(float a, float b, float n) {
    if(n >= 1) return b;
    if(n <= 0) return a;
    return a + (b - a) * n;
  }

  public static float lerp(float minT, float maxT, float t, float outMin, float outMax) {
    if (t <= minT) return outMin;
    if (t >= maxT) return outMax;
    float scale = (t - minT) / (maxT - minT);
    return outMin + (scale * (outMax - outMin));
  }
}
