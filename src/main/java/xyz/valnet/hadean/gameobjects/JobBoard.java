package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.gameobjects.worldobjects.pawn.Pawn;
import xyz.valnet.hadean.interfaces.IWorkable;
import xyz.valnet.hadean.util.Pair;

public class JobBoard extends GameObject {

  private Set<Job> availableJobs = new HashSet<Job>();
  private List<Job> toRemove = new ArrayList<Job>();
  private Map<Pawn, Job> allocations = new HashMap<Pawn, Job>();

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
      List<Pawn> toFire = new ArrayList<Pawn>();

      for(Pawn worker : allocations.keySet()) {
        if(allocations.get(worker) == job) {
          toFire.add(worker);
        }
      }

      for(Pawn worker : toFire) {
        allocations.remove(worker);
      }
    }

    if(availableJobs.contains(job)) {
      availableJobs.remove(job);
    }

    job.close();
  }

  public boolean jobsAvailableForWorker(Pawn worker) {
    return getJobsForWorker(worker).size() != 0;
  }

  private List<Job> getJobsForWorker(Pawn worker) {
    Vector2i workerLocation = worker.getWorldPosition().xy();

    List<Job> workables = availableJobs
      .stream()
      .filter(job -> job.isValid())
      .map(job -> new Pair<Job, Float>(
        job,
        Stream.of(job.getLocations())
          .map(v -> v.distanceTo((int) workerLocation.x, (int) workerLocation.y))
          .reduce(Float.MAX_VALUE, (a, b) -> a < b ? a : b)
      ))
      // sort the jobs by their distance from the worker
      .sorted((Pair<Job, Float> a, Pair<Job, Float> b) -> {
        if(a.second() > b.second()) return 1;
        if(b.second() > a.second()) return -1;
        return 0;
      })
      // then convert the stream back to just the jobs
      .map(workerDistanceTuple -> workerDistanceTuple.first())
      .toList();

    return workables;
  }

  public Job requestJob(Pawn worker) {

    
    List<Job> workables = getJobsForWorker(worker);
    
    if(workables.size() > 0) {
      Job firstJob = workables.get(0);
      availableJobs.remove(firstJob);
      allocations.put(worker, firstJob);
      return firstJob;
    }
    return null;
  }

  public void completeJob(Job job) {
    this.rescindJob(job);
  }

  public void quitJob(Pawn worker, Job job) {
    if(!allocations.containsKey(worker)) return;
    Job foundJob = allocations.get(worker);
    if(foundJob != job) return;
    availableJobs.add(job);
    allocations.remove(worker);
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

  public boolean workerHasJob(Pawn worker) {
    return allocations.containsKey(worker);
  }
  
  // public Job getJob(Pawn worker) {
  //   if(allocations.containsKey(worker)) {
  //     return allocations.get(worker);
  //   } else return null;
  // }

  public String details() {
    
    String takenJobsString = "";
    String availableJobsString = "";

    for(Entry<Pawn, Job> allocation : allocations.entrySet()) {
      takenJobsString += "  " + allocation.getKey().getName() + ": " + allocation.getValue().getJobName() + "\n";
    }
    
    for(Job job : availableJobs) {
      availableJobsString += "  " + job.getJobName() + "\n";
    }

    return "Available Jobs: " + availableJobs.size() + "\n" + availableJobsString +
           "Taken Jobs: " + allocations.size() + "\n" + takenJobsString;
  }

}
