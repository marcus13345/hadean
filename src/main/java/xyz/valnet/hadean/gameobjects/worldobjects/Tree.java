package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Tree extends WorldObject implements ITileThing, ISelectable, IWorkable {

  private static int counter = 0;
  private String name = "Tree " + (++ counter);

  private boolean chopFlag = false;
  
  private int x, y;

  public Tree(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render() {
    Assets.flat.pushColor(new Vector4f(1 - getProgress(), 1 - getProgress(), 1 - getProgress(), 1.0f));
    camera.draw(Layers.AIR, Assets.tree, x - 1, y - 2, 3, 3);
    Assets.flat.popColor();
    if(hasWork()) {
      camera.draw(Layers.MARKERS, Assets.lilAxe, x, y);
    }
  }

  @Override
  public boolean isWalkable() {
    return false;
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
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
      if(chopFlag) {
        get(JobBoard.class).postJob(this);
      } else {
        get(JobBoard.class).rescindJob(this);
      }
    }
  }

  @Override
  public boolean hasWork() {
    return chopFlag && choppage < strength;
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    return new Vector2i[] {
      new Vector2i(x, y - 1),
      new Vector2i(x, y + 1),
      new Vector2i(x - 1, y),
      new Vector2i(x + 1, y)
    };
  }

  protected float choppage = 0;
  protected int strength = 300;

  private float getProgress() {
    return (choppage / (float) strength);
  }

  @Override
  public void doWork() {
    choppage ++;
  }

  @Override
  public String details() {
    return "" + name + "\n" +
           "Chop Flag | " + chopFlag + "\n" +
           "Progress  | " + (String.format("%.2f", getProgress() * 100)) + "%";
  }

  @Override
  public void update(float dTime) {
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

  @Override
  public Vector2i getLocation() {
    return new Vector2i(x, y);
  }

  @Override
  public String getJobName() {
    return "Chop " + name;
  }
}
