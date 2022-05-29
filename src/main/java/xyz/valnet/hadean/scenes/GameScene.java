package xyz.valnet.hadean.scenes;

import xyz.valnet.engine.scenegraph.SceneGraph;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Selection;
import xyz.valnet.hadean.gameobjects.SelectionUI;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.tabs.ArchitectTab;
import xyz.valnet.hadean.gameobjects.tabs.MenuTab;
import xyz.valnet.hadean.gameobjects.worldobjects.Pawn;

// TODO BIG IDEAS
// have caches of types that ill need (Like IMouseListener)
// and the add method, simply puts things into the newObjects
// then at the beginning of each frame, they are all loaded.
// then, even userspace scenes can add all their objects in 
// one fell swoop and the timings will be maintained
// IE: all objects will be linked and able to be `get`ed
// at their start call! :D

public class GameScene extends SceneGraph {

  @Override
  protected void construct() {
    
    objects.add(new Terrain());
    objects.add(new Camera());

    for(int i = 0; i < 5; i ++) {
      objects.add(new Pawn());
    }
    
    objects.add(new Selection());
    objects.add(new SelectionUI());

    objects.add(new BottomBar());
    objects.add(new ArchitectTab());
    objects.add(new MenuTab());
    
  }
}
