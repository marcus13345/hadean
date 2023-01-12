package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import java.util.EnumSet;

import xyz.valnet.engine.graphics.Tile16.Direction;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Job;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.Buildable;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Boulder;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.BuildableMetadata;
import xyz.valnet.hadean.interfaces.IItemReceiver;
import xyz.valnet.hadean.interfaces.IPingable;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.detail.BooleanDetail;
import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.ObjectDetail;
import xyz.valnet.hadean.util.detail.PercentDetail;

@BuildableMetadata(category = "Structure", name = "Wall", type = BuildableMetadata.Type.SINGLE)
public class Wall extends Buildable implements IItemReceiver, IWorkable, IPingable {

  private int boulders = 0;
  private float work = 0;
  private final float maxWork = 500;

  private Job job = null;

  @Override
  protected void create() {
    super.create();
    job = add(new Job("Build Wall"));
    job.addStep(job.new PickupItemByPredicate(Boulder.BOULDER_PREDICATE));
    job.addStep(job.new DropoffPredicateAtItemReceiver(this, Boulder.BOULDER_PREDICATE));
    job.addStep(job.new Work(this));
    get(JobBoard.class).postJob(job);
  }

  @Override
  protected void start() {
    super.start();
    ping();
  }

  @Override
  public void render() {
    super.render();
    Vector2i pos = getWorldPosition().xy();

    if(isBuilt()) {
      float b = 0.7f;
      Assets.flat.pushColor(new Vector4f(b, b, b, 1f));
      camera.draw(Layers.GROUND, Assets.wall.getTextureFor(wallSides), pos.x, pos.y);
      Assets.flat.popColor();
    } else {
      float p = work / maxWork;
      float b = 4;

      Assets.flat.pushColor(new Vector4f(b, b, b, 0.5f));
      camera.draw(Layers.GROUND, Assets.wall.getTextureFor(wallSides), pos.x, pos.y);
      Assets.flat.popColor();

      if(boulders > 0) {
        camera.drawProgressBar(p, getWorldBox());
      }
    }
  }

  @Override
  public String getName() {
    return "Wall";
  }

  @Override
  public boolean receive(Item item) {
    if(item == null) return false;
    if(!item.matches(Boulder.BOULDER_PREDICATE)) return false;
    remove(item);
    boulders ++;
    return true;
  }

  private boolean isBuilt() {
    return work >= maxWork;
  }

  @Override
  public boolean doWork(float dTime) {
    work += dTime;
    return isBuilt();
  }

  private Vector2i[] getBorders() {
    Vector2i pos = getWorldPosition().xy();
    return new Vector2i[] {
      pos.north(),
      pos.east(),
      pos.south(),
      pos.west()
    };
  }

  @Override
  public Vector2i[] getWorkablePositions() {
    return getBorders();
  }

  @Override
  public String getJobName() {
    return "Build Wall";
  }

  @Override
  public Vector2i[] getItemDropoffLocations() {
    return getBorders();
  }

  @Override
  public Action[] getActions() {
    return new Action[0];
  }

  @Override
  public void runAction(Action action) {

  }

  @Override
  public Detail[] getDetails() {
    return new Detail[] {
      new BooleanDetail("Built", isBuilt()),
      new PercentDetail("Work", work / maxWork, 1),
      new ObjectDetail<Integer>("Logs", boulders),
      new BooleanDetail("North", wallSides.contains(Direction.NORTH)),
      new BooleanDetail("East", wallSides.contains(Direction.EAST)),
      new BooleanDetail("South", wallSides.contains(Direction.SOUTH)),
      new BooleanDetail("West", wallSides.contains(Direction.WEST)),
    };
  }

  @Override
  public boolean isWalkable() {
    return isBuilt();
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {
    
  }

  private EnumSet<Direction> wallSides = EnumSet.noneOf(Direction.class);

  @Override
  public void ping() {
    Vector2i pos = getWorldBox().asInt().xy();
    wallSides = EnumSet.noneOf(Direction.class);
    
    Tile north = terrain.getTile(pos.x, pos.y - 1);
    if(north != null && north.has(Wall.class)) wallSides.add(Direction.NORTH);
    
    Tile east = terrain.getTile(pos.x - 1, pos.y);
    if(east != null && east.has(Wall.class)) wallSides.add(Direction.EAST);
    
    Tile south = terrain.getTile(pos.x, pos.y + 1);
    if(south != null && south.has(Wall.class)) wallSides.add(Direction.SOUTH);
    
    Tile west = terrain.getTile(pos.x + 1, pos.y);
    if(west != null && west.has(Wall.class)) wallSides.add(Direction.WEST);
  }
}
