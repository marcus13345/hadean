package xyz.valnet.hadean.gameobjects.ui.tabs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.valnet.engine.graphics.Drawing;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.engine.scenegraph.IMouseCaptureArea;
import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.Camera;
import xyz.valnet.hadean.gameobjects.inputlayer.BuildLayer;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.input.IButtonListener;
import xyz.valnet.hadean.input.SimpleButton;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IBuildLayerListener;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ISelectionChangeListener;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.SmartBoolean;

public class BuildTab extends Tab implements ISelectionChangeListener, IMouseCaptureArea, IButtonListener {
  
  private SelectionLayer selection;
  private BuildLayer buildLayer;
  private Camera camera;

  private SmartBoolean opened;
  private int width = 200;

  private int padding = 10;

  private int x, y;
  private int w, h;

  private String selectedCategory = "";

  private transient BuildableRecord selectedBuildable = null;
  private transient Map<String, List<BuildableRecord>> buildables = null;
  private transient Map<Button, BuildableRecord> buildableButtons = null;

  private int height = 0;

  private record BuildableRecord(
    String name,
    Constructor<? extends IBuildable> constructor,
    int type
  ) {

  }

  @SuppressWarnings("unchecked")
  private void calculateBuildables() {
    try {
      Class<?>[] maybeBuildables = getClasses("xyz.valnet.hadean");

      for(Class<?> clazz : maybeBuildables) {
        if(clazz.isAnonymousClass()) continue;
        if(!IBuildable.class.isAssignableFrom(clazz)) continue;
        if(clazz.isInterface()) continue;
        if(Modifier.isAbstract(clazz.getModifiers())) continue;

        Constructor<? extends IBuildable> constructor = (Constructor<? extends IBuildable>) clazz.getConstructor();
        if(constructor.getParameterCount() != 0) {
          System.out.println(clazz + " has no default constructor (no params)");
          continue;
        }
        BuildableMetadata annotation = clazz.getAnnotation(BuildableMetadata.class);
        if(annotation == null) {
          System.out.println(clazz + " has no buildable data annotation");
          continue;
        }
        String category = annotation.category();
        String name = annotation.name();
        int type = annotation.type();

        if(!buildables.containsKey(category))
          buildables.put(category, new ArrayList<BuildableRecord>());
        buildables.get(category).add(new BuildableRecord(name, constructor, type));

        System.out.println("Added " + category + " / " + name);
      }

    } catch (Exception e) {
      System.out.println(e);
    }

    height = Math.max((int)Math.ceil(buildables.size() / 2f) * 24, 24*3);
  }

  @Override
  public void renderAlpha () {
    Drawing.setLayer(Layers.GENERAL_UI);

    if(opened.value()) {
      Assets.uiFrame.draw(padding, 576 - BottomBar.bottomBarHeight - padding - height, width, height);

      if(selectedBuildable == null) return;
      // draw the currently selected build item
      Assets.flat.pushColor(new Vector4f(1f, 1f, 1f, 1.0f));
      Vector2i topLeft = camera.world2screen(x, y).asInt();
      Assets.font.drawString(selectedBuildable.name, topLeft.x, topLeft.y - 20);
      Assets.flat.swapColor(new Vector4f(1f, 1f, 1f, 0.5f));
      for(int i = 0; i < w; i ++) for(int j = 0; j < h; j ++) {{
        camera.draw(Layers.BUILD_INTERACTABLE, Assets.checkerBoard, x + i, y + j);
      }}
      Assets.flat.popColor();
    }
  }

  @Override
  public void start() {
    super.start();
    buildLayer = get(BuildLayer.class);
    selection = get(SelectionLayer.class);
    camera = get(Camera.class);

    opened = new SmartBoolean(false, new SmartBoolean.IListener() {

      @Override
      public void rise() {
        activate();
      }

      @Override
      public void fall() {
        deactiveate();
      }

    });

    if(selection != null) {
      selection.subscribe(this);
    }

    buildables = new HashMap<String, List<BuildableRecord>>();
    buildableButtons = new HashMap<Button, BuildableRecord>();

    calculateBuildables();
  }

  private List<Button> categoryButtons = new ArrayList<Button>();

  private void deselectBuilding() {
    if(selectedBuildable != null) buildLayer.deactiveate();
    selectedBuildable = null;
  }

  private void selectBuildable(BuildableRecord buildableRecord) {
    if(buildableRecord == null) deselectBuilding();
    if (selectedBuildable == null) activateBuildLayer();
    selectedBuildable = buildableRecord;
  }

  private void activateBuildLayer() {
    buildLayer.activate(new IBuildLayerListener() {
      @Override
      public void update(int nx, int ny, int nw, int nh) {
        x = nx;
        y = ny;
        w = nw;
        h = nh;
      }

      @Override
      public void build(int x1, int y1, int x2, int y2) {
        if(selectedBuildable == null) return;
        try {
          IBuildable building = selectedBuildable.constructor.newInstance();
          if(building instanceof GameObject) {
            add((GameObject) building);
          }
          building.buildAt(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        } catch (Exception e) {
          System.out.println(e);
        }

        // opened.set(false);
      }

      @Override
      public void cancel() {
        if(selectedBuildable == null) {
          opened.set(false);
          buildLayer.deactiveate();
        } else {
          deselectBuilding();
        }
      }
    });
  }

  private void activate() {

    int i = 0;
    categoryButtons.clear();
    for(String c : buildables.keySet()) {
      int left = i % 2 == 0 ? padding : padding + width / 2;
      int y = 576 - BottomBar.bottomBarHeight - padding - height + ((int)Math.floor(i / 2)) * 24;
      Button b = new SimpleButton(c, left, y, width / 2, 24, Layers.GENERAL_UI_INTERACTABLE);
      b.registerClickListener(this);
      categoryButtons.add(b);
      add(b);
      i ++;
    }

    constructItemButtons();
  }

  private void deactiveate() {
    buildLayer.deactiveate();
    if(categoryButtons != null) for(Button btn : categoryButtons) remove(btn);
    categoryButtons.clear();
    constructItemButtons();
  }

  @Override
  public void update(float dTime) {
    
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
    return new Vector4f(padding, 576 - BottomBar.bottomBarHeight - padding - height, width, height);
  }

  @Override
  public float getLayer() {
    return Layers.GENERAL_UI;
  }

  private void constructItemButtons() {
    for(Button btn : buildableButtons.keySet()) {
      remove(btn);
    }

    buildableButtons.clear();

    if(!opened.value()) return;

    List<BuildableRecord> categoryBuildables = buildables.get(selectedCategory);
    if(categoryBuildables == null) return;

    int left = width + padding * 2;
    int buttonHeight = 24;
    int buttonWidth = 100;
    int top = 576 - BottomBar.bottomBarHeight - padding - buttonHeight;
    int i = 0;
    for(BuildableRecord buildableRecord : categoryBuildables) {
      int x = left + (buttonWidth + padding) * i;
      int y = top;
      int w = buttonWidth;
      int h = buttonHeight;
      i ++;
      Button btn = new SimpleButton(buildableRecord.name, x, y, w, h, Layers.GENERAL_UI_INTERACTABLE);
      btn.registerClickListener(this);
      add(btn);
      buildableButtons.put(btn, buildableRecord);
    }
  }

  @Override
  public void click(Button target) {
    if(categoryButtons.contains(target)) {
      selectedCategory = target.getText();
      deselectBuilding();
      constructItemButtons();
    } else if(buildableButtons.containsKey(target)) {
      BuildableRecord newBuildableRecord = buildableButtons.get(target);
      selectBuildable(newBuildableRecord);
    }
  }

  @SuppressWarnings("rawtypes")
  private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      assert classLoader != null;
      String path = packageName.replace('.', '/');
      Enumeration<URL> resources = classLoader.getResources(path);
      List<File> dirs = new ArrayList<File>();
      while (resources.hasMoreElements()) {
          URL resource = resources.nextElement();
          dirs.add(new File(resource.getFile()));
      }
      ArrayList<Class> classes = new ArrayList<Class>();
      for (File directory : dirs) {
          classes.addAll(findClasses(directory, packageName));
      }
      return (Class<?>[]) classes.toArray(new Class[classes.size()]);
  }
  
  @SuppressWarnings("rawtypes")
  private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<Class>();
    if (!directory.exists()) {
        return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
        if (file.isDirectory()) {
            assert !file.getName().contains(".");
            classes.addAll(findClasses(file, packageName + "." + file.getName()));
        } else if (file.getName().endsWith(".class")) {
            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
        }
    }
    return classes;
  }
}
