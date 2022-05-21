package xyz.valnet.hadean.gameobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class Tree extends GameObject implements ITileThing, ISelectable, IWorkable {
  private Camera camera;
  private Terrain terrain;

  private boolean chopFlag = false;
  
  private int x, y;

  public Tree(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void start() {
    camera = get(Camera.class);
    terrain = get(Terrain.class);
  }

  @Override
  public void render() {
    Assets.flat.pushColor(new Vector4f(1 - getProgress(), 1 - getProgress(), 1 - getProgress(), 1.0f));
    camera.draw(Assets.tree, x - 1, y - 2, 3, 3);
    Assets.flat.popColor();
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x - 1, y - 2, x + 2, y + 1);
  }

  public static final Action ACTION_CHOP = new Action("Chop");

  @Override
  public Action[] getActions() {
    return new Action[] {
      ACTION_CHOP
    };
  }

  @Override
  public void runAction(Action action) {
    if(action == ACTION_CHOP) {
      chopFlag = !chopFlag;
    }
  }

  @Override
  public boolean hasWork() {
    return chopFlag && choppage < strength;
  }

  @Override
  public Vector2i[] getWorablePositions() {
    return new Vector2i[] {
      new Vector2i(x, y - 1),
      new Vector2i(x, y + 1),
      new Vector2i(x - 1, y),
      new Vector2i(x + 1, y)
    };
  }

  protected float choppage = 0;
  protected int strength = 500;

  private float getProgress() {
    return (choppage / (float) strength);
  }

  @Override
  public void doWork() {
    choppage ++;
  }

  @Override
  public String details() {
    return "Chop Flag | " + chopFlag + "\n" +
           "Progress  | " + (String.format("%.2f", getProgress() * 100)) + "%";
  }

  @Override
  public void tick(float dTime) {
    if(choppage >= strength) {
      return;
    }
    if(choppage > 0) {
      choppage -= 0.01f;
    } else {
      choppage = 0;
    }
    
  }

  @Override
  public boolean shouldRemove() {
    return getProgress() >= 1.0;
  }

  @Override
  public void onRemove() {
    add(new Log(x, y));
  }
}
