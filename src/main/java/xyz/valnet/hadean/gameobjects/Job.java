package xyz.valnet.hadean.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.worldobjects.Stockpile;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.IItemReceiver;
import xyz.valnet.hadean.interfaces.IWorkable;

public class Job extends GameObject {

  private Job that = this;
  private List<Callback> closedListeners = new ArrayList<Callback>();

  public abstract class JobStep implements Serializable {
    public abstract Vector2i[] getLocations();
    public void next() {
      that.nextStep();
    }
    public abstract boolean isValid();
  }

  public class PickupItem extends JobStep {
    public Item item;
    
    public PickupItem(Item item) {
      this.item = item;
    }

    @Override
    public Vector2i[] getLocations() {
      return new Vector2i[] { item.getWorldPosition().xy() };
    }

    @Override
    public boolean isValid() {
      return true;
    }
  }

  public class DropoffPredicateAtItemReceiver extends JobStep {

    public IItemReceiver receiver;
    public IItemPredicate predicate;

    public DropoffPredicateAtItemReceiver(IItemReceiver receiver, IItemPredicate predicate) {
      this.receiver = receiver;
      this.predicate = predicate;
    }

    @Override
    public Vector2i[] getLocations() {
      return receiver.getItemDropoffLocations();
    }

    @Override
    public boolean isValid() {
      return true;
    }
  }

  public class PickupItemByPredicate extends JobStep {
    public IItemPredicate predicate;
    
    public PickupItemByPredicate(IItemPredicate predicate) {
      this.predicate = predicate;
    }

    @Override
    public Vector2i[] getLocations() {
      Set<Vector2i> positionSet = new HashSet<Vector2i>();
      for(Item item : that.getAll(Item.class)) {
        if(!item.matches(predicate)) continue;
        positionSet.add(item.getWorldPosition().xy());
      }
      Vector2i[] positions = new Vector2i[positionSet.size()];
      positionSet.toArray(positions);
      return positions;
    }

    @Override
    public boolean isValid() {
      return getLocations().length > 0;
    }
  }

  public class DropoffAtItemReceiver extends JobStep {

    public IItemReceiver receiver;
    public Item item;

    public DropoffAtItemReceiver(IItemReceiver receiver, Item item) {
      this.receiver = receiver;
      this.item = item;
    }

    @Override
    public Vector2i[] getLocations() {
      return receiver.getItemDropoffLocations();
    }

    @Override
    public boolean isValid() {
      return true;
    }
  }

  // TODO find the _best_ place to dropoff, instead of just the top left place.
  public class DropoffAtStockpile extends JobStep {
    public Item item;
    public DropoffAtStockpile(Item item) {
      this.item = item;
    }

    public Vector2i[] getLocations() {
      Stockpile pile = that.get(Stockpile.class);
      // Vector4f box = pile.getWorldBox().toXYWH();
      return new Vector2i[] {
        pile.getFreeTile()
      };
    }

    @Override
    public boolean isValid() {
      return that.get(Stockpile.class) != null;
    }
  }

  public class Work extends JobStep {
    public IWorkable subject;
    public Work(IWorkable subject) {
      this.subject = subject;
    }
    @Override
    public Vector2i[] getLocations() {
      return subject.getWorkablePositions();
    }
    public boolean doWork(float dTime) {
      return subject.doWork(dTime);
    }

    @Override
    public boolean isValid() {
      return true;
    }
  }

  private List<JobStep> steps;
  private String name;
  private int step;

  public void reset() {
    step = 0;
  }

  public Job(String name) {
    this.steps = new ArrayList<JobStep>();
    this.name = name;
  }

  public void addStep(JobStep step) {
    steps.add(step);
  }

  public Vector2i[] getLocations() {
    if(steps.size() == 0) throw new Error("Cannot get location of job with no steps");
    JobStep step = steps.get(0);
    return step.getLocations();
  }

  public void nextStep() {
    step ++;
    if(isCompleted()) {
      get(JobBoard.class).completeJob(this);
      for(Callback callback : closedListeners) {
        callback.apply();
      }
      remove(this);
    }
  }

  public boolean isCompleted() {
    return step >= steps.size();
  }

  public JobStep getCurrentStep() {
    if(step >= steps.size()) return null;
    return steps.get(step);
  }

  public String getJobName() {
    return name;
  }

  @FunctionalInterface
  public interface Callback extends Serializable {
    public void apply();
  }

  public void close() {
    for(Callback callback : closedListeners) {
      callback.apply();
    }
  }

  public void registerClosedListener(Callback callback) {
    closedListeners.add(callback);
  }

  public void unregisterClosedListener(Callback callback) {
    closedListeners.remove(callback);
  }

  public boolean isValid() {
    for(JobStep step : steps) {
      if(!step.isValid()) return false;
    }
    return true;
  }
}
