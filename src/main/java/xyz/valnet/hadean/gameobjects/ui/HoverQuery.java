package xyz.valnet.hadean.gameobjects.ui;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.App;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.HadeanGame;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.WorldObject;
import xyz.valnet.hadean.util.Assets;

public class HoverQuery extends GameObject implements ITransient {

  private Camera camera;

  @Override
  public void start() {
    super.start();
    camera = get(Camera.class);
  }

  private List<String> thingStrings = new ArrayList<String>();

  @Override
  public void update(float dTime) {
    Vector2f position = camera.screen2world(App.mouseX, App.mouseY);
    thingStrings.clear();
    for(WorldObject obj : getAll(WorldObject.class)) {
      Vector4f box = obj.getWorldBox();
      if(
        position.x >= box.x &&
        position.x < box.z &&
        position.y >= box.y &&
        position.y < box.w
      ) {
        thingStrings.add(obj.getName());
        if (!HadeanGame.debugView) continue;
        
        if(obj instanceof Tile) {
          thingStrings.add(((Tile)obj).toThingsString());
        }
      }
    }
  }
  
  @Override
  public void render() {
    int i = 16;
    for(String thingString : thingStrings) {
      for(String str : thingString.split("\n")) {
        Assets.font.drawString(str, 16, i);
        i += 14;
      }
    }
  }
}
