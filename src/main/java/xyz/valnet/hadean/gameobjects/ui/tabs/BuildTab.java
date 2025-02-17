package xyz.valnet.hadean.gameobjects.ui.tabs;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.TileBox;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.designation.CutTreesDesignation;
import xyz.valnet.hadean.designation.HaulItemDesignation;
import xyz.valnet.hadean.gameobjects.ui.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.inputlayer.BuildLayer;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.gameobjects.worldobjects.zones.FarmPlot;
import xyz.valnet.hadean.gameobjects.worldobjects.zones.Stockpile;
import xyz.valnet.hadean.gameobjects.worldobjects.constructions.Bed;
import xyz.valnet.hadean.gameobjects.worldobjects.constructions.MasonWorkshop;
import xyz.valnet.hadean.gameobjects.worldobjects.constructions.Quarry;
import xyz.valnet.hadean.gameobjects.worldobjects.constructions.Wall;
import xyz.valnet.hadean.interfaces.BuildType;
import xyz.valnet.hadean.interfaces.IBuildLayerListener;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class BuildTab extends Tab implements ISelectionChangeListener, IBuildLayerListener {
  
  private SelectionLayer selection;
  private BuildLayer buildLayer;
  private Camera camera;

  private TileBox renderBox = null;

  private String selectedCategory = null;

  private static transient Map<String, List<BuildableRecord>> buildables = new HashMap<String, List<BuildableRecord>>();
  private transient BuildableRecord selectedBuildable = null;

  static {
    BuildTab.registerBuildable(HaulItemDesignation.class);
    BuildTab.registerBuildable(CutTreesDesignation.class);

    BuildTab.registerBuildable(Bed.class);

    BuildTab.registerBuildable(Wall.class);

    BuildTab.registerBuildable(Quarry.class);
    
    BuildTab.registerBuildable(FarmPlot.class);
    BuildTab.registerBuildable(Stockpile.class);
    BuildTab.registerBuildable(MasonWorkshop.class);
  }

  public record BuildableRecord(
    String name,
    Constructor<? extends IBuildable> constructor,
    BuildType type,
    Vector2i dimensions
  ) {}

  public static void registerBuildable(Class<? extends IBuildable> clazz) {
    try {
      // BuildableMetadata annotation = clazz.getAnnotation(BuildableMetadata.class);
      // if(annotation == null) {
        // DebugTab.log(clazz + " has no buildable data annotation");
        // return;
      // }

      Constructor<? extends IBuildable> constructor = (Constructor<? extends IBuildable>) clazz.getConstructor();
      if(constructor.getParameterCount() != 0) {
        DebugTab.log(clazz + " has no default constructor (no params)");
        return;
      }

      IBuildable buildable = constructor.newInstance();

      String category = buildable.getBuildTabCategory();
      String name = buildable.getBuildTabName();
      BuildType type = buildable.getBuildType();
      Vector2i dim = buildable.getDimensions();

      DebugTab.log("Added " + category + " / " + name);

      if(!buildables.containsKey(category))
        buildables.put(category, new ArrayList<BuildableRecord>());
      buildables.get(category).add(new BuildableRecord(name, constructor, type, dim));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void renderAlpha () {
    Drawing.setLayer(Layers.GENERAL_UI);

    if(!opened || selectedBuildable == null) return;
    // draw the currently selected build item
    
    Assets.flat.pushColor(Color.white);
    Vector2i topLeft = camera.world2screen(renderBox.topLeft);
    Assets.font.drawString(selectedBuildable.name, topLeft.x, topLeft.y - 20);
    
    Assets.flat.swapColor(Color.white.withAlpha(0.6f));
    camera.draw(Layers.BUILD_INTERACTABLE, Assets.selectionFrame, renderBox);
    
    Assets.flat.swapColor(Color.white.withAlpha(0.35f));
    for(int i = 0; i < renderBox.w; i ++) for(int j = 0; j < renderBox.h; j ++) {{
      camera.draw(Layers.BUILD_INTERACTABLE, Assets.checkerBoard, renderBox.x + i, renderBox.y + j);
    }}

    Assets.flat.popColor();
  }

  @Override
  protected void connect() {
    super.connect();
    buildLayer = get(BuildLayer.class);
    selection = get(SelectionLayer.class);
    camera = get(Camera.class);
  }

  @Override
  public void start() {
    super.start();
    if(selection != null) {
      selection.subscribe(this);
    }
  }

  @Override
  public void back() {
    if(selectedBuildable != null) {
      selectBuildable(null);
    } else {
      close();
    }
  }

  private void selectBuildable(BuildableRecord buildableRecord) {
    if(buildableRecord == null) {
      buildLayer.deactiveate();
      selectedBuildable = null;
      return;
    }
    selectedBuildable = buildableRecord;
    buildLayer.activate(this);
    buildLayer.setBuildType(selectedBuildable.type);
    buildLayer.setDimensions(selectedBuildable.dimensions);
  }

  @Override
  public void cancel() {
    if(selectedBuildable == null) {
      close();
    } else {
      selectBuildable(null);
    }
  }

  @Override
  public void selectionChanged(List<ISelectable> selected) {
    if(selected.isEmpty()) return;
    close();
  }

  private void reset() {
    selectBuildable(null);
  }

  private void selectCategory(String category) {
    selectedCategory = category;
    selectBuildable(null);
  }

  @Override
  public String getTabName() {
    return "Build";
  }

  public void gui() {
    if(!shouldRender()) return;

    int height = 8 + 16 + 8 + buildables.size() * (32 + 8);

    window(animate(-180, 0), 576 - BottomBar.bottomBarHeight - height + 1, 150, height, () -> {
      text("Build");

      for(String category : buildables.keySet()) {
        space(8);
        if(button(category)) {
          if(selectedCategory == category) {
            selectCategory(null);
          } else {
            selectCategory(category);
          }
        }
      }
    });

    window(149, animate(576 + 50, 576 - BottomBar.bottomBarHeight - 16 - 32 + 1 - 24), 875, 48 + 24, () -> {
      if(selectedCategory == null) {
        space(20);
        text("    Select a Category...");
        return;
      }
      text(selectedCategory);
      space(8);
      horizontal(() -> {
        for(BuildableRecord buildableRecord : buildables.get(selectedCategory)) {
          if(button(buildableRecord.name)) {
            selectBuildable(buildableRecord);
          }
          space(8);
        }
      });
    });
  }

  @Override
  protected void onClose() {
    buildLayer.deactiveate();
  }

  @Override
  protected void onOpen() {
    reset();
  }

  @Override
  public void update(TileBox box) {
    renderBox = box;
  }

  @Override
  public void build(TileBox box) {
    if(selectedBuildable == null) return;
    try {
      IBuildable building = selectedBuildable.constructor.newInstance();
      if(building instanceof GameObject) {
        add((GameObject) building);
      }
      building.buildAt(box);
    } catch (Exception e) {
      DebugTab.log(e);
    }
  }
}
