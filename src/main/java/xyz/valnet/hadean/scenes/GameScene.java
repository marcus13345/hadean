package xyz.valnet.hadean.scenes;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IScene;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Pawn;
import xyz.valnet.hadean.gameobjects.Selection;
import xyz.valnet.hadean.gameobjects.SelectionUI;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.tabs.ArchitectTab;
import xyz.valnet.hadean.gameobjects.tabs.MenuTab;

public class GameScene implements IScene {

  // generic
  private List<GameObject> objects = new ArrayList<GameObject>();
  private List<GameObject> newObjects = new ArrayList<GameObject>();
  private List<GameObject> removeObjects = new ArrayList<GameObject>();
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

  public <T> List<T> getAll(Class<T> clazz) {
    List<T> stuff = new ArrayList<T>();
    for(GameObject obj : objects) {
      if(clazz.isInstance(obj)) {
        stuff.add(clazz.cast(obj));
      }
    }
    return stuff;
  }

  @Override
  public void render() {
    for(GameObject obj : objects) {
      obj.render();
    }
  }

  @Override
  public void update(float dTime) {
    if(!newObjects.isEmpty()) {
      List<GameObject> added = new ArrayList<GameObject>();

      for(GameObject obj : newObjects) {
        objects.add(obj);
        added.add(obj);
      }
      newObjects.clear();

      for(GameObject obj : added) {
        obj.start();
      }
    }

    if(!removeObjects.isEmpty()) {
      for(GameObject obj : removeObjects) {
        objects.remove(obj);
      }
      removeObjects.clear();
    }

    for(GameObject obj : objects) {
      obj.tick(dTime);
    }
  }

  @Override
  public void enable() {
    objects.add(new Terrain());
    for(int i = 0; i < 5; i ++) {
      objects.add(new Pawn());
    }
    objects.add(new Camera());
    objects.add(new Selection());
    objects.add(new SelectionUI());
    objects.add(new BottomBar());
    objects.add(new ArchitectTab());
    objects.add(new MenuTab());
    
    for(GameObject obj : objects) {
      obj.link(this);
    }

    for(GameObject obj : objects) {
      obj.start();
    }
  }

  @Override
  public void disable() {
    objects.clear();
  }

  public void add(GameObject obj) {
    newObjects.add(obj);
    obj.link(this);
  }

  public void remove(GameObject obj) {
    removeObjects.add(obj);
  }

  public boolean inScene(GameObject gameObject) {
    return objects.contains(gameObject);
  }
  
}
