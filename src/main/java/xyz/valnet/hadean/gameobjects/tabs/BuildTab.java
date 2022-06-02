package xyz.valnet.hadean.gameobjects.tabs;

import static xyz.valnet.engine.util.Math.lerp;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.inputlayer.BuildLayer;
import xyz.valnet.hadean.gameobjects.inputlayer.Selection;
import xyz.valnet.hadean.gameobjects.worldobjects.FarmPlot;
import xyz.valnet.hadean.interfaces.IBuildLayerListener;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.SmartBoolean;

public class BuildTab extends Tab implements ISelectionChangeListener, IMouseCaptureArea {
  
  private Selection selection;
  private BuildLayer buildLayer;
  private Camera camera;
  private Terrain terrain;

  private SmartBoolean opened;
  private float progress = 0f;
  private float width = 200;

  private int padding = 10;

  private int x, y;

  @Override
  public void render() {
    Drawing.setLayer(Layers.GENERAL_UI);
    float left = lerp(-width - padding, padding, progress);
    Assets.uiFrame.draw((int) left, padding, (int) width, 576 - padding * 2 - BottomBar.bottomBarHeight);

    if(opened.value()) {
      Drawing.setLayer(Layers.BUILD_INTERACTABLE);
      // draw the currently selected build item
      Assets.flat.pushColor(new Vector4f(1f, 1f, 1f, 0.8f));
      camera.draw(Assets.stockpile, x, y);
      Assets.flat.popColor();
    }
  }

  @Override
  public void start() {
    super.start();
    buildLayer = get(BuildLayer.class);
    selection = get(Selection.class);
    camera = get(Camera.class);
    terrain = get(Terrain.class);
    
    IBuildLayerListener buildListener = new IBuildLayerListener() {
      @Override
      public void update(float nx, float ny, float nw, float nh) {
        x = (int)Math.floor(nx);
        y = (int)Math.floor(ny);
      }

      @Override
      public void select(float nx, float ny, float nw, float nh) {
        x = (int)Math.floor(nx);
        y = (int)Math.floor(ny);
        ITileThing thing = new FarmPlot(x, y);
        terrain.getTile(x, y).placeThing(thing);
        opened.set(false);
      }

      @Override
      public void cancel() {
        opened.set(false);
      }
    };

    opened = new SmartBoolean(false, new SmartBoolean.IListener() {

      @Override
      public void rise() {
        buildLayer.activate(buildListener);
      }

      @Override
      public void fall() {
        buildLayer.deactiveate();
      }

    });

    if(selection != null) {
      selection.subscribe(this);
    }
  }

  @Override
  public void update(float dTime) {
    progress = lerp(progress, opened.value() ? 1 : 0, 0.05f);
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    if(selected.isEmpty()) return;
    opened.set(false);
  }

  @Override
  public void evoke() {
    opened.toggle();

    if(opened.value()) {
      selection.updateSelection(new ArrayList<ISelectable>());
    }
  }

  @Override
  public String getTabName() {
    return "Build";
  }

  @Override
  public void mouseEnter() {}

  @Override
  public void mouseLeave() {}

  @Override
  public void mouseDown(int button) {}

  @Override
  public void mouseUp(int button) {}

  @Override
  public Vector4f getBox() {
    float left = lerp(-width - padding, padding, progress);
    return new Vector4f((int) left, padding, (int) width, 576 - padding * 2 - BottomBar.bottomBarHeight);
  }

  @Override
  public int getLayer() {
    return Layers.GENERAL_UI;
  }
}
