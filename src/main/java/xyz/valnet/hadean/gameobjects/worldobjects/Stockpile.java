package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IBuildable;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

@BuildableMetadata(category = "Zones", name = "Stockpile")
public class Stockpile extends WorldObject implements IWorkable, ISelectable, ITileThing, IBuildable {

  private JobBoard board;

  private int w, h;

  @Override
  public void render() {
  }

  @Override
  public void renderAlpha() {
    if(!visible) return;
    Assets.flat.pushColor(new Vector4f(1f, 0.2f, 0.1f, 0.3f));
    camera.draw(Layers.GROUND, Assets.whiteBox, x, y, w, h);
    Assets.flat.popColor();
  }

  @Override
  public void update(float dTime) {
    super.update(dTime);

  }

  @Override
  public void start() {
    super.start();
    board = get(JobBoard.class);
    board.postJob(this);
  }

  @Override
  public boolean hasWork() {
    return false;
  }

  @Override
  public Vector2i[] getWorkablePositions() {
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
    return "No jobs here!";
  }

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

  @Override
  public void doWork() {
    
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
