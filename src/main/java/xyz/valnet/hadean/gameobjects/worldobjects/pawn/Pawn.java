package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import static xyz.valnet.hadean.util.detail.Detail.mergeDetails;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Clock;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.worldobjects.agents.Agent;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.Pair;
import xyz.valnet.hadean.util.detail.BooleanDetail;
import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.ObjectDetail;
import xyz.valnet.hadean.util.detail.PercentDetail;

public class Pawn extends Agent {

  private static int pawnCount = 0;
  private String name = "Pawn " + (++ pawnCount);
  private Needs needs = new Needs();

  // private int meaningless = 0;

  // private float workEthic = (float) Math.random();
  // private float selfWorth = (float) Math.random();

  private List<Activity> activities = new ArrayList<Activity>();
  private Activity currentActivity = null;

  public void pickupItem(Item i) {
    Item item = getTile().removeThing(i);
    if(item == null) return;
    remove(item);
    inventory.add(item);
  }

  public void dropoffItem(Item item) {
    if(!inventory.contains(item)) {
      return;
    }
    inventory.remove(item);
    add(item);
    getTile().placeThing(item);
  }

  @Override
  public void start() {
    super.start();
    x = (int) (Math.random() * Terrain.WORLD_SIZE);
    y = (int) (Math.random() * Terrain.WORLD_SIZE);

    activities.add(new JobActivity(this, get(JobBoard.class)));
    activities.add(new SleepActivity(this, needs, get(Clock.class)));
    // activities.add(new WanderActivity());
  }

  @Override
  public void render() {
    super.render();
    if(currentActivity instanceof SleepActivity) {
      Assets.flat.pushColor(new Vector4f(0.5f, 0.5f, 0.5f, 1.0f));
    } else {
      Assets.flat.pushColor(Vector4f.one);
    }
    camera.draw(Layers.PAWNS, Assets.pawn, getCalculatedPosition());
    Assets.flat.popColor();
  }

  @Override
  public void runAction(Action action) {}

  @Override
  public Detail[] getDetails() {
    // return needs.getDetails();
    return mergeDetails(needs.getDetails(), new Detail[] {
      new ObjectDetail<Activity>("Activity", currentActivity),
      new PercentDetail("Sleep Value", activities.get(1).getBenefit(), 2),
      new BooleanDetail("Pathing", isPathing())
    });
  }

  @Override
  public Vector2f getWorldPosition() {
    return new Vector2f(x, y);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Vector4f getWorldBox() {
    Vector2f pos = getCalculatedPosition();
    return new Vector4f(pos.x, pos.y, pos.x+1, pos.y+1);
  }

  @Override
  protected void think() {
    super.think();
    decideActivity();
    pathToActivity();
  }

  private void pathToActivity() {
    if(currentActivity == null) return;
    Vector2i[] locations = currentActivity.getTargetLocations();
    if(locations == null || locations.length == 0) return;
    if(isPathing() && getDestination().isOneOf(locations)) return;
    goToClosest(locations);
  }

  private void decideActivity() {
    
    List<Activity> urgentActivities = activities.stream()
      .filter(a -> a.isValid() && a.isUrgent())
      .toList();

    if(urgentActivities.size() > 0) {
      switchActivity(urgentActivities.get(0));
      return;
    }

    if(currentActivity != null) return;

    currentActivity = null;

    List<Activity> valueSortedActivities = activities.stream()
      .filter(a -> a.isValid())
      .map(a -> new Pair<Activity, Float>(a, a.getBenefit()))
      .filter(a -> a.second() >= 0)
      .sorted((a, b) -> a.second() > b.second() ? -1 : 1)
      .map(p -> p.first())
      .toList();

    if(valueSortedActivities.size() == 0) return;

    switchActivity(valueSortedActivities.get(0));
  }

  private void switchActivity(Activity activity) {
    if(currentActivity != null) currentActivity.end();
    currentActivity = activity;
    stopPathing();
    currentActivity.begin(a -> endActivity(a));
  }

  private void endActivity() {
    endActivity(currentActivity);
  }

  private void endActivity(Activity activity) {
    activity.end();
    stopPathing();
    if(currentActivity == activity) {
      currentActivity = null;
    }
  }

  // TODO at some point rewrite this to use an actor component array
  // where we loop through until something _does_ sometihng.
  @Override
  protected boolean act() {
    if(super.act()) return true;
    // if(doJob()) return true;
    if(currentActivity != null) {
      currentActivity.act();
      return true;
    }
    return false;
  }

  protected void postAct() {
    needs.update(1);
  }

  private List<Item> inventory = new ArrayList<Item>();
}
