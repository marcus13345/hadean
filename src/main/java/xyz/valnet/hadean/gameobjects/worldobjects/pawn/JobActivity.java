package xyz.valnet.hadean.gameobjects.worldobjects.pawn;

import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.Job;
import xyz.valnet.hadean.gameobjects.Job.JobStep;
import xyz.valnet.hadean.gameobjects.JobBoard;

public class JobActivity extends Activity {

  private JobBoard jobboard;
  private Pawn worker;
  private Job job;

  public JobActivity(Pawn worker, JobBoard jobboard) {
    this.worker = worker;
    this.jobboard = jobboard;
  }

  @Override
  public boolean isUrgent() {
    return false;
  }

  @Override
  public boolean isValid() {
    return jobboard.jobsAvailableForWorker(worker);
  }

  @Override
  public float getBenefit() {
    return 0.5f;
  }

  @Override
  public void act(float dTime) {
    if (doJob(dTime)) return;
  }


  private boolean isPathingToJobLocation() {
    if(!worker.isPathing()) return false;
    return worker.getDestination().isOneOf(job.getCurrentStep().getLocations());
  }

  private boolean isAtJobStepLocation() {
    return worker.getWorldPosition().asInt().isOneOf(job.getCurrentStep().getLocations());
  }

  private void goToJobStepLocation() {
    worker.goToClosest(job
      .getCurrentStep()
      .getLocations()
    );
  }

  ActivityCancellationCallback callback;

  @Override
  public void begin(ActivityCancellationCallback callback) {

    this.callback = callback;
    job = jobboard.requestJob(worker);
    if(job == null) callback.apply(this);
    job.registerClosedListener(() -> {
      System.out.println("job cancelled");
      callback.apply(this);
    });

    // if we dont have a job
    if(!jobboard.workerHasJob(worker)) {
    }

    //      if we have a job           and need to go to it    and we're not pathing to it
    if(jobboard.workerHasJob(worker) && !isAtJobStepLocation() && !isPathingToJobLocation()) {
      goToJobStepLocation(); // start pathing there.
      return; // and dont think about anything else.
    }
  }

  @Override
  public void end() {
    jobboard.quitJob(worker, job);
    job = null;
  }

  private boolean doJob(float dTime) {
    if(!jobboard.workerHasJob(worker)) return false;
    JobStep step = job.getCurrentStep();
    // if we're not at the location of the job...
    if(!isAtJobStepLocation()) return false;

    if(step instanceof Job.Work) {
      Job.Work workStep = (Job.Work)step;
      if(workStep.doWork(dTime)) step.next();
      return true;
    } else if(step instanceof Job.PickupItem) {
      Job.PickupItem pickupStep = (Job.PickupItem) step;
      worker.pickupItem(pickupStep.item);
      step.next();
      return true;
    } else if(step instanceof Job.DropoffAtStockpile) {
      if(!worker.getTile().isTileFree()) return false;
      Job.DropoffAtStockpile dropoffStep = (Job.DropoffAtStockpile) step;
      worker.dropoffItem(dropoffStep.item);
      step.next();
      return true;
    }

    return false;
  }

  @Override
  public String toString() {
    if(job == null) return "Activity (Null Job)";
    return job.getJobName();
  }

  @Override
  public Vector2i[] getTargetLocations() {
    return job.getCurrentStep().getLocations();
  }
  
}
