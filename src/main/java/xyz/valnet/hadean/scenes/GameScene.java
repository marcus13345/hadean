package xyz.valnet.hadean.scenes;

import xyz.valnet.engine.scenegraph.SceneGraph;
import xyz.valnet.engine.util.Names;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Clock;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.SelectionUI;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.inputlayer.BuildLayer;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.gameobjects.ui.HoverQuery;
import xyz.valnet.hadean.gameobjects.ui.Popup;
import xyz.valnet.hadean.gameobjects.ui.tabs.BuildTab;
import xyz.valnet.hadean.gameobjects.ui.tabs.JobBoardTab;
import xyz.valnet.hadean.gameobjects.ui.tabs.LoadTab;
import xyz.valnet.hadean.gameobjects.ui.tabs.MenuTab;
import xyz.valnet.hadean.gameobjects.ui.tabs.SaveTab;
import xyz.valnet.hadean.gameobjects.worldobjects.pawn.Pawn;

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

    Names.loadNames();
    
    objects.add(new Terrain());
    objects.add(new Camera());
    objects.add(new JobBoard());
    objects.add(new Clock());

    for(int i = 0; i < 5; i ++) {
      objects.add(new Pawn());
    }

    objects.add(new SelectionLayer());
    // objects.add(new SelectionUI());

    objects.add(new BuildLayer());


    objects.add(new HoverQuery());

    objects.add(new BottomBar());
    objects.add(new BuildTab());
    objects.add(new JobBoardTab());
    objects.add(new MenuTab());
    objects.add(new SaveTab());
    objects.add(new LoadTab());

    objects.add(new Popup());
    
  }
}
