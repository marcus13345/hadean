package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

@BuildableMetadata(category = "Zones", name = "Farm Plot")
public class FarmPlot extends WorldObject implements ISelectable, ITileThing, IBuildable {

  // private float progress = 0f;
  // private int stage = 0;
  // private boolean planted = false;
  // private boolean mature = false;

  // private static int STAGE_LENGTH = 1000;
  // private static int MAX_STAGES = 4;

  // private JobBoard board;

  private int w, h;

  @Override
  public void render() {
    // camera.draw(Layers.TILES, Assets.farmPlot, x, y);

    // if(planted) {
    //   if(stage > 1) {
    //     camera.draw(Layers.AIR, Assets.growingRice[stage], x, y - 1, 1, 2);
    //   } else {
    //     camera.draw(Layers.AIR, Assets.growingRice[stage], x, y);
    //   }
    // }
  }

  @Override
  public void renderAlpha() {
    if(!visible) return;
    Assets.flat.pushColor(new Vector4f(0.4f, 1f, 0.3f, 0.2f));
    camera.draw(Layers.GROUND, Assets.whiteBox, x, y, w, h);
    Assets.flat.popColor();
  }

  @Override
  public void update(float dTime) {
    super.update(dTime);

    // if(stage == MAX_STAGES - 1) {
    //   return;
    // } if(planted) {
    //   if(Math.random() > 0.95f) {
    //     progress += 10;
    //     if(progress >= STAGE_LENGTH) {
    //       stage ++;
    //       progress = 0;
    //       if(stage == MAX_STAGES - 1) {
    //         mature = true;
    //         board.postJob(this);
    //       }
    //     }
    //   }
    // } else if (progress >= STAGE_LENGTH) {
    //   planted = true;
    //   progress = 0;
    // }


  }

  @Override
  public void start() {
    super.start();
    // board = get(JobBoard.class);
    // board.postJob(this);
  }

  // @Override
  // public boolean hasWork() {
  //   // return !planted || mature;
  // }

  // @Override
  // public Vector2i[] getWorkablePositions() {
  //   return new Vector2i[] {
  //     new Vector2i((int) x, (int) y + 1),
  //     new Vector2i((int) x, (int) y - 1),
  //     new Vector2i((int) x + 1, (int) y),
  //     new Vector2i((int) x - 1, (int) y)
  //   };
  // }

  // @Override
  // public Vector2i getLocation() {
  //   return new Vector2i((int) x, (int) y);
  // }

  // @Override
  // public String getJobName() {
  //   return planted ? "Harvest Rice" : "Plant Rice";
  // }

  @Override
  public Vector4f getWorldBox() {
    return new Vector4f(x, y, x + w, y + h);
  }

  private static Action TOGGLE_VISIBILITY = new Action("Hide / Show");

  @Override
  public Action[] getActions() {
    return new Action[] { TOGGLE_VISIBILITY };
  }

  private boolean visible = true;

  @Override
  public void runAction(Action action) {
    if(action == TOGGLE_VISIBILITY) {
      visible = !visible;
    }
  }

  @Override
  public String details() {
    
    return "";
  }

  // @Override
  // public void doWork() {
  //   // progress ++;
  //   // if(mature && progress >= STAGE_LENGTH) {
  //   //   mature = false;
  //   //   planted = false;
  //   //   stage = 0;
  //   //   if(Math.random() < 0.3) {
  //   //     getTile().placeThing(new Rice((int)x, (int)y));
  //   //   }
  //   // }
  // }

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
  public void buildAt(int x, int y, int w, int h) {
    System.out.println("buildAt");
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    System.out.println("<" + x + ", " + y + ", " + w + ", " + h + ">");
    System.out.println(inScene());
    terrain.getTile(x, y).placeThing(this);
    for(int i = x; i < x + w; i ++) {
      for(int j = y; j < y + h; j ++) {
        terrain.getTile(i, j).placeThing(this);
      }
    }
  }

}
