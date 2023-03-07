package xyz.valnet.hadean.gameobjects.jobs;

import java.util.HashSet;
import java.util.Set;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Boulder;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Item;
import xyz.valnet.hadean.interfaces.IItemReceiver;
import xyz.valnet.hadean.interfaces.IWorkshop;

public class WorkOrder extends GameObject {
  
  private boolean recurring = false;
  private int count = 10;
  private String name = "Cut Stone Blocks";
  private IWorkshop shop = null;
  private float maxWork = 1000;

  private Set<Job> relatedJobs;

  public boolean getRecurring() {
    return recurring;
  }

  public int getCount() {
    return count;
  }

  public String getName() {
    return name;
  }

  public float getMaxWork() {
    return maxWork;
  }

  public void increaseCount() {
    count ++;
  }

  public void decreaseCount() {
    count --;
  }

  public WorkOrder setShop(IWorkshop shop) {
    this.shop = shop;
    return this;
  }

  public boolean validForShop(IWorkshop shop) {
    return this.shop == null || this.shop == shop;
  }

  public boolean isSpecificToShop(IWorkshop shop) {
    return this.shop != null && this.shop == shop;
  }

  @Override
  protected void create() {
    super.create();
    relatedJobs = new HashSet<>();
  }

  public boolean isBeingWorkedOn() {
    return relatedJobs.size() > 0;
  }

  public boolean canCreateJob() {
    return count > relatedJobs.size();
  }

  public Job createNextJob(IWorkshop shop) {
    Job job = new Job("Cut brickies");
    job.addStep(job.new PickupItemByPredicate(Boulder.BOULDER_PREDICATE));
    job.addStep(job.new DropoffPredicateAtItemReceiver(new IItemReceiver() {
      @Override
      public boolean receive(Item item) {
        
        return false;
      }

      @Override
      public Vector2i[] getItemDropoffLocations() {
        return shop.getWorkablePositions();
      }
    }, Boulder.BOULDER_PREDICATE));
    job.addStep(job.new Work(shop));
    job.registerCompletedListener(() -> {
      decreaseCount();

    });
    job.registerClosedListener(() -> {
      relatedJobs.remove(job);
    });
    get(JobBoard.class).postJob(job);
    return job;
  }
}
