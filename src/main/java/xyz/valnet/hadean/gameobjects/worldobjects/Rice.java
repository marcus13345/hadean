package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.interfaces.ISelectable;
import xyz.valnet.hadean.interfaces.ITileThing;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Rice extends WorldObject implements ITileThing, ISelectable {

  // private SmartBoolean haul;

  private JobBoard jobboard;

  @Override
  public void start() {
    super.start();
    jobboard = get(JobBoard.class);
    Rice that = this;

    // haul = new SmartBoolean(false, new IListener() {
    //   @Override
    //   public void rise() {
    //     jobboard.postJob(that);
    //   }

    //   @Override
    //   public void fall() {
    //     jobboard.rescindJob(that);
    //   }
    // });
  }

  public Rice(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public void render() {
    camera.draw(Layers.AIR, Assets.riceBag, x, y);

    Assets.flat.pushColor(Vector4f.black);
    Vector2f screeCoords = camera.world2screen(x, y);
    Assets.miniFont.drawString("123", (int)screeCoords.x, (int)screeCoords.y);
    Assets.flat.popColor();
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

  @Override
  public Action[] getActions() {
    return new Action[] {};
  }

  @Override
  public void runAction(Action action) {
  }

  @Override
  public String details() {
    return "Bag of Rice";
  }

  // @Override
  // public boolean hasWork() {
  //   return haul.value();
  // }

  // @Override
  // public Vector2i[] getWorablePositions() {
  //   return new Vector2i[] {
  //     new Vector2i((int)x + 1, (int)y),
  //     new Vector2i((int)x - 1, (int)y),
  //     new Vector2i((int)x, (int)y + 1),
  //     new Vector2i((int)x, (int)y - 1)
  //   };
  // }

  // @Override
  // public Vector2i getLocation() {
  //   return new Vector2i((int)x, (int)y);
  // }

  // @Override
  // public Log take() {
  //   haul.set(false);
  //   Tile tile = terrain.getTile((int)x, (int)y);
  //   tile.remove(this);
  //   return this;
  // }

  // @Override
  // public Tile getDestination() {
  //   return get(Stockpile.class).getTile();
  // }
  
  // @Override
  // public String getJobName() {
  //   return "Haul Log";
  // }
  
}
