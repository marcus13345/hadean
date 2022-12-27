package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;
import java.util.Set;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.interfaces.IWorker;
import xyz.valnet.hadean.util.Pair;

public class JobBoard extends GameObject {

  private Set<Job> availableJobs = new HashSet<Job>();
  private List<Job> toRemove = new ArrayList<Job>();
  private Map<IWorker, Job> allocations = new HashMap<IWorker, Job>();

  public Job postSimpleWorkJob(String name, IWorkable subject) {
    Job job = add(new Job(name));
    job.addStep(job.new Work(subject));
    postJob(job);
    return job;
  }

  public void postJob(Job job) {
    availableJobs.add(job);
  }

  public void rescindJob(Job job) {
    if(allocations.values().contains(job)) {
      List<IWorker> toFire = new ArrayList<IWorker>();

      for(IWorker worker : allocations.keySet()) {
        if(allocations.get(worker) == job) {
          toFire.add(worker);
        }
      }

      for(IWorker worker : toFire) {
        allocations.remove(worker);
      }
    }

    if(availableJobs.contains(job)) {
      availableJobs.remove(job);
    }
  }

  public void requestJob(IWorker worker) {
    // TODO worker has capabilities?
    Vector2f workerLocation = worker.getWorldPosition();

    List<Job> workables = availableJobs
      .stream()
      .map(job -> new Pair<Job, Float>(
        job,
        Stream.of(job.getLocations())
          .map(v -> v.distanceTo((int) workerLocation.x, (int) workerLocation.y))
          .reduce(Float.MAX_VALUE, (a, b) -> a < b ? a : b)
      ))
      // sort the jobs by their distance from the worker
      .sorted(new Comparator<Pair<Job, Float>>() {
        @Override
        public int compare(Pair<Job, Float> a, Pair<Job, Float> b) {
          if(a.second() > b.second()) return 1;
          if(b.second() > a.second()) return -1;
          return 0;
        }
      })
      // then convert the stream back to just the jobs
      .map(workerDistanceTuple -> workerDistanceTuple.first())
      .toList();
    
    
    if(workables.size() > 0) {
      Job firstJob = workables.get(0);
      availableJobs.remove(firstJob);
      allocations.put(worker, firstJob);
      return;
    }
  }

  public void completeJob(Job job) {
    this.rescindJob(job);
  }

  public void completeJob(IWorker worker) {
    if(!workerHasJob(worker)) return;
    rescindJob(getJob(worker));
  }

  @Override
  public void update(float dTime) {
    for(Job job : toRemove) {
      if(allocations.values().contains(job)) {
        // I AM NOT SURE THIS WORKS
        allocations.values().remove(job);
      }
      if(availableJobs.contains(job)) {
        availableJobs.remove(job);
      }
    }
    toRemove.clear();
  }

  public boolean workerHasJob(IWorker worker) {
    return allocations.containsKey(worker);
  }
  
  public Job getJob(IWorker worker) {
    if(allocations.containsKey(worker)) {
      return allocations.get(worker);
    } else return null;
  }

  public String details() {
    
    String takenJobsString = "";
    String availableJobsString = "";

    for(Entry<IWorker, Job> allocation : allocations.entrySet()) {
      takenJobsString += "  " + allocation.getKey().getName() + ": " + allocation.getValue().getJobName() + "\n";
    }
    
    for(Job job : availableJobs) {
      availableJobsString += "  " + job.getJobName() + "\n";
    }

    return "Available Jobs: " + availableJobs.size() + "\n" + availableJobsString +
           "Taken Jobs: " + allocations.size() + "\n" + takenJobsString;
  }

}
