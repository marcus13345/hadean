package xyz.valnet.hadean.scenes;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IScene;
import xyz.valnet.hadean.gameobjects.Terrain;

public class GameScene implements IScene {

  // generic
  private List<GameObject> objects = new ArrayList<GameObject>();
  // private List<IRenderable> renderables = new ArrayList<IRenderable>();

  // specific
  private GameObject terrain;

  @Override
  public void render() {
    for(GameObject obj : objects) {
      ((Terrain)obj).render();
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
    terrain = new Terrain(this);
    objects.add(terrain);
  }

  @Override
  public void disable() {
    objects.clear();
  }
  
}
