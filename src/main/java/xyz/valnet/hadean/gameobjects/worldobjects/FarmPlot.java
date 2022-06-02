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

public class FarmPlot extends WorldObject implements IWorkable, ISelectable, ITileThing {

  private float progress = 0f;
  private int stage = 0;
  private boolean planted = false;
  private boolean mature = false;

  private static int STAGE_LENGTH = 1000;
  private static int MAX_STAGES = 4;

  private JobBoard board;

  public FarmPlot(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render() {
    camera.draw(Layers.GROUND, Assets.farmPlot, x, y);

    if(planted) {
      if(stage > 1) {
        camera.draw(Layers.AIR, Assets.growingRice[stage], x, y - 1, 1, 2);
      } else {
        camera.draw(Layers.AIR, Assets.growingRice[stage], x, y);
      }
    }
  }

  @Override
  public void update(float dTime) {
    super.update(dTime);

    if(stage == MAX_STAGES - 1) {
      return;
    } if(planted) {
      if(Math.random() > 0.95f) {
        progress += 10;
        if(progress >= STAGE_LENGTH) {
          stage ++;
          progress = 0;
          if(stage == MAX_STAGES - 1) {
            mature = true;
            board.postJob(this);
          }
        }
      }
    } else if (progress >= STAGE_LENGTH) {
      planted = true;
      progress = 0;
    }


  }

  @Override
  public void start() {
    super.start();
    board = get(JobBoard.class);
    board.postJob(this);
  }

  @Override
  public boolean hasWork() {
    return !planted || mature;
  }

  @Override
  public Vector2i[] getWorablePositions() {
    return new Vector2i[] {
      new Vector2i((int) x, (int) y + 1),
      new Vector2i((int) x, (int) y - 1),
      new Vector2i((int) x + 1, (int) y),
      new Vector2i((int) x - 1, (int) y)
    };
  }

  @Override
  public Vector2i getLocation() {
    return new Vector2i((int) x, (int) y);
  }

  @Override
  public String getJobName() {
    return planted ? "Harvest Rice" : "Plant Rice";
  }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + 1, y + 1);
  }

  @Override
  public Action[] getActions() {
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {}

  @Override
  public String details() {
    
    return "Planted  | " + planted + "\n" +
           "Stage    | " + stage + "\n" +
           "Progress | " + String.format("%.2f", (progress / STAGE_LENGTH) * 100) + "%";
  }

  @Override
  public void doWork() {
    progress ++;
    if(mature && progress >= STAGE_LENGTH) {
      mature = false;
      planted = false;
      stage = 0;
      if(Math.random() < 0.3) {
        getTile().placeThing(new Rice((int)x, (int)y));
      }
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
  public void onRemove() {
    
  }

  @Override
  public void updatePosition(int x, int y) {}


}
