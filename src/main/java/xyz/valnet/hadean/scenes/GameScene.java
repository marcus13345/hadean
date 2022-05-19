package xyz.valnet.hadean.scenes;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IScene;
import xyz.valnet.hadean.gameobjects.Pawn;
import xyz.valnet.hadean.gameobjects.Terrain;

public class GameScene implements IScene {

  // generic
  private List<GameObject> objects = new ArrayList<GameObject>();
  // private List<IRenderable> renderables = new ArrayList<IRenderable>();

  // specific

  public <T> T get(Class<T> clazz) {
    for(GameObject obj : objects) {
      if(clazz.isInstance(obj)) {
        return clazz.cast(obj);
      }
    }
    return null;
  }

  @Override
  public void render() {
    for(GameObject obj : objects) {
      obj.render();
    }
  }

  @Override
  public void update(float dTime) {
    for(GameObject obj : objects) {
      obj.tick(dTime);
    }
  }

  @Override
  public void enable() {
    objects.add(new Terrain(this));
    for(int i = 0; i < 3; i ++) {
      objects.add(new Pawn(this));
    }
  }

  @Override
  public void disable() {
    objects.clear();
  }
  
}
