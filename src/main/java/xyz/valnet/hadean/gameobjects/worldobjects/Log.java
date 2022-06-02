package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Stockpile;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.interfaces.IHaulable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.SmartBoolean;
import xyz.valnet.hadean.util.SmartBoolean.IListener;

public class Log extends WorldObject implements ITileThing, ISelectable, IHaulable {

  private SmartBoolean haul;

  private JobBoard jobboard;

  @Override
  public void start() {
    super.start();
    jobboard = get(JobBoard.class);
    Log that = this;

    haul = new SmartBoolean(false, new IListener() {
      @Override
      public void rise() {
        jobboard.postJob(that);
      }

      @Override
      public void fall() {
        jobboard.rescindJob(that);
      }
    });
  }

  public Log(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render() {
    camera.draw(Layers.GROUND, Assets.log, x, y);
    if(haul.value()) {
      camera.draw(Layers.MARKERS, Assets.haulArrow, x, y);
    }
  }

  @Override
  public boolean isWalkable() {
    return true;
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {}

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
  }

  private static final Action ACTION_HAUL = new Action("Haul");

  @Override
  public Action[] getActions() {
    return new Action[] {
      ACTION_HAUL
    };
  }

  @Override
  public void runAction(Action action) {
    if(action == ACTION_HAUL) {
      haul.toggle();
    }
  }

  @Override
  public String details() {
    return "A fat log";
  }

  @Override
  public boolean hasWork() {
    return haul.value();
  }

  @Override
  public Vector2i[] getWorablePositions() {
    return new Vector2i[] {
      new Vector2i((int)x + 1, (int)y),
      new Vector2i((int)x - 1, (int)y),
      new Vector2i((int)x, (int)y + 1),
      new Vector2i((int)x, (int)y - 1)
    };
  }

  @Override
  public Vector2i getLocation() {
    return new Vector2i((int)x, (int)y);
  }

  @Override
  public Log take() {
    haul.set(false);
    Tile tile = terrain.getTile((int)x, (int)y);
    tile.remove(this);
    return this;
  }

  @Override
  public Tile getDestination() {
    return get(Stockpile.class).getTile();
  }

  @Override
  public void updatePosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String getJobName() {
    return "Haul Log";
  }
  
}
