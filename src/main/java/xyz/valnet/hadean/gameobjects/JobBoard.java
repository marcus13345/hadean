package xyz.valnet.hadean.gameobjects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.scenegraph.GameObject;
import xyz.valnet.hadean.interfaces.IJob;
import xyz.valnet.hadean.interfaces.IWorker;
import xyz.valnet.hadean.pathfinding.IPathfinder;
import xyz.valnet.hadean.pathfinding.Path;
import xyz.valnet.hadean.util.Pair;

public class JobBoard extends GameObject {

  private Set<IJob> availableJobs = new HashSet<IJob>();
  private Map<IWorker, IJob> allocations = new HashMap<IWorker, IJob>();

  public void postJob(IJob job) {
    availableJobs.add(job);
  }

  public void rescindJob(IJob job) {
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
    Vector2f workerLocation = worker.getLocation();
    IPathfinder pathfinder = worker.getPathfinder();

    List<IJob> workables = availableJobs
      .stream()
      // filter available job by the ones that currently have work
      // TODO seems like this should be removed at some point
      // and jobs should post / rescind when they need to
      .filter(workable -> workable.hasWork())
      // calculate our workers distance to each
      .map(workable -> new Pair<IJob, Float>(
        workable,
        workable.getLocation().distanceTo(
          (int) workerLocation.x,
          (int) workerLocation.y
        )
      ))
      // sort the jobs by their distance from the worker
      .sorted(new Comparator<Pair<IJob, Float>>() {
        @Override
        public int compare(Pair<IJob, Float> a, Pair<IJob, Float> b) {
          if(a.second() > b.second()) return 1;
          if(b.second() > a.second()) return -1;
          return 0;
        }
      })
      // then convert the stream back to just the jobs
      .map(workerDistanceTuple -> workerDistanceTuple.first())
      .toList();
    
    
    if(workables.size() > 0) {
      for(IJob job : workables) {
        if(!job.hasWork()) continue;
        Vector2i[] workablePositions = job.getWorkablePositions();
        Path bestPathToJob = pathfinder.getBestPath(
          new Vector2i((int)Math.floor(workerLocation.x), (int)Math.floor(workerLocation.y)),
          workablePositions
        );
        if(bestPathToJob == null) continue;

        // it is decided. job is good, and path is hype
        worker.setPath(bestPathToJob);
        availableJobs.remove(job);
        allocations.put(worker, job);
        return;
      }
    }
  }

  @Override
  public void update(float dTime) {
    List<IJob> toRemove = new ArrayList<IJob>();
    for(IJob job : allocations.values()) {
      if(!job.hasWork()) {
        toRemove.add(job);
      }
    }
    for(IJob job : toRemove) {
      rescindJob(job);
    }
  }

  public IJob getJob(IWorker worker) {
    if(allocations.containsKey(worker)) {
      return allocations.get(worker);
    } else return null;
  }

  public String details() {
    
    String takenJobsString = "";
    String availableJobsString = "";

    for(Entry<IWorker, IJob> allocation : allocations.entrySet()) {
      takenJobsString += "  " + allocation.getKey().getName() + ": " + allocation.getValue().getJobName() + "\n";
    }
    
    for(IJob job : availableJobs) {
      availableJobsString += "  " + job.getJobName() + "\n";
    }

    return "Available Jobs: " + availableJobs.size() + "\n" + availableJobsString +
           "Taken Jobs: " + allocations.size() + "\n" + takenJobsString;
  }

}
