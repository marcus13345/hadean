package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.jobs.Job;
import xyz.valnet.hadean.gameobjects.jobs.JobBoard;
import xyz.valnet.hadean.gameobjects.terrain.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Log;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.BooleanDetail;
import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.PercentDetail;

public class Tree extends WorldObject implements ITileThing, ISelectable, IWorkable {

  private Job chopJob = null;

  public Tree(int x, int y) {
    setPosition(x, y);
  }

  @Override
  public void render() {
    Vector2i pos = getWorldPosition().xy();
    // Assets.flat.pushColor(new Vector4f(1 - getProgress(), 1 - getProgress(), 1 - getProgress(), 1.0f));
    camera.draw(Layers.AIR, Assets.tree, pos.x - 1, pos.y - 2, 3, 3);
    // Assets.flat.popColor();
    if(chopJob != null) {
      if(getProgress() > 0) {
        camera.drawProgressBar(getProgress(), new Vector4f(pos.x - 1, pos.y - 2, pos.x + 2, pos.y + 1));
      }
      camera.draw(Layers.MARKERS, Assets.lilAxe, pos.x, pos.y);
    }
  }

  @Override
  public boolean isWalkable() {
    return false;
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
      if(chopJob == null) {
        chopJob = get(JobBoard.class).postSimpleWorkJob(this);
      } else {
        chopJob.close();
        chopJob = null;
      }
    }
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    Vector2i pos = getWorldPosition().xy();
    return new Vector2i[] {
      new Vector2i(pos.x, pos.y - 1),
      new Vector2i(pos.x, pos.y + 1),
      new Vector2i(pos.x - 1, pos.y),
      new Vector2i(pos.x + 1, pos.y)
    };
  }

  protected float choppage = 0;
  protected int strength = 300;

  private float getProgress() {
    return (choppage / (float) strength);
  }

  @Override
  public boolean doWork(float dTime) {
    choppage += dTime;
    return getProgress() >= 1;
  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {
      new BooleanDetail("Chop Flag", chopJob != null),
      new PercentDetail("Progress", getProgress())
    };
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
    Vector2i pos = getWorldPosition().xy();
    add(new Log(pos.x, pos.y));
  }

  @Override
  public String getJobName() {
    return "Chop Tree";
  }

  @Override
  public String getName() {
    return "Tree";
  }

  @Override
  public void onPlaced(Tile tile) {}
}
