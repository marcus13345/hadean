package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import static xyz.valnet.hadean.util.detail.Detail.*;

import java.util.ArrayList;
import java.util.List;

import xyz.valnet.engine.graphics.Color;
import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.util.Names;
import xyz.valnet.hadean.HadeanGame;
import xyz.valnet.hadean.gameobjects.Clock;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.worldobjects.agents.Agent;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.IItemReceiver;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;
import xyz.valnet.hadean.util.Pair;
import xyz.valnet.hadean.util.detail.BooleanDetail;
import xyz.valnet.hadean.util.detail.Detail;
import xyz.valnet.hadean.util.detail.ObjectDetail;
import xyz.valnet.hadean.util.detail.PercentDetail;

public class Pawn extends Agent {

  private String name = Names.getRandomName();
  private Needs needs = new Needs();

  // private int meaningless = 0;

  // private float workEthic = (float) Math.random();
  // private float selfWorth = (float) Math.random();

  private transient List<Activity> activities = new ArrayList<Activity>();
  private Activity currentActivity = null;

  public void pickupItemByPredicate(IItemPredicate itemPredicate) {
    Item item = getTile().pickupByItemPredicate(itemPredicate);
    inventory.add(item);
  }

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
    inventory.remove(add(item));
    item.setPosition(getWorldPosition().xy());
    getTile().placeThing(item);
  }

  public void dropoffItem(Item item, IItemReceiver receiver) {
    if(!inventory.contains(item)) {
      return;
    }
    inventory.remove(item);
    add(item);
    receiver.receive(item);
  }

  private Item getInventoryItemByPredicate(IItemPredicate predicate) {
    for(Item item : inventory) {
      if(!item.matches(predicate)) continue;
      return item;
    }
    return null;
  }

  public void dropoffPredicate(IItemPredicate predicate, IItemReceiver receiver) {
    Item item = getInventoryItemByPredicate(predicate);
    if(!inventory.contains(item)) {
      return;
    }
    dropoffItem(item, receiver);
  }

  @Override
  protected void ready() {
    super.ready();
    activities = new ArrayList<Activity>();
  }

  @Override
  public void start() {
    super.start();

    activities.add(new JobActivity(this, get(JobBoard.class)));
    activities.add(new SleepActivity(needs, get(Clock.class)));
    activities.add(new WanderActivity(get(Terrain.class)));
  }

  protected void create() {
    setPosition(
      (int) (Math.random() * Terrain.WORLD_SIZE),
      (int) (Math.random() * Terrain.WORLD_SIZE)
    );
  }

  @Override
  public void render() {
    super.render();
    if(currentActivity instanceof SleepActivity) {
      Assets.flat.pushColor(Color.grey(0.5f));
    } else {
      Assets.flat.pushColor(Color.white);
    }
    camera.draw(Layers.PAWNS, Assets.pawn, getCalculatedPosition());
    Assets.flat.popColor();
  }

  @Override
  public void runAction(Action action) {}

  @Override
  public Detail[] getDetails() {
    // return needs.getDetails();
    Detail[] details = mergeDetails(needs.getDetails(), new Detail[] {
      new ObjectDetail<Activity>("Activity", currentActivity),
      new BooleanDetail("Pathing", isPathing()),
      new ObjectDetail<Integer>("Inventory", inventory.size())
    });

    if(HadeanGame.debugView) {
      for(Activity activity : activities) {
        details = mergeDetails(details, new Detail[] {
          new PercentDetail(activity.toString(), activity.getBenefit())
        });
      }
    }

    return details;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getGenericName() {
    return "Pawn";
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
    if(activity == currentActivity) return;
    if(currentActivity != null) currentActivity.end();
    currentActivity = activity;
    stopPathing();
    currentActivity.begin(a -> endActivity(a));
  }

  private void endActivity(Activity activity) {
    activity.end();
    stopPathing();
    if(currentActivity == activity) {
      currentActivity = null;
    }
  }

  @Override
  protected boolean act(float dTime) {
    if(super.act(dTime)) return true;
    if(currentActivity != null) {
      currentActivity.act(dTime);
      return true;
    }
    return false;
  }

  protected void postAct() {
    needs.update(1);
  }

  private List<Item> inventory = new ArrayList<Item>();
}
