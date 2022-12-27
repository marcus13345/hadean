package xyz.valnet.hadean.gameobjects.worldobjects;

import xyz.valnet.engine.math.Vector2f;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.hadean.gameobjects.Job;
import xyz.valnet.hadean.gameobjects.JobBoard;
import xyz.valnet.hadean.gameobjects.Terrain;
import xyz.valnet.hadean.gameobjects.Job.JobStep;
import xyz.valnet.hadean.gameobjects.worldobjects.agents.Agent;
import xyz.valnet.hadean.interfaces.IWorker;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;
import xyz.valnet.hadean.util.Layers;

public class Pawn extends Agent implements IWorker {

  private static int pawnCount = 0;
  private String name = "Pawn " + (++ pawnCount);

  @Override
  public void start() {
    super.start();
    jobboard = get(JobBoard.class);
    x = (int) (Math.random() * Terrain.WORLD_SIZE);
    y = (int) (Math.random() * Terrain.WORLD_SIZE);
  }

  @Override
  public void render() {
    super.render();
    camera.draw(Layers.PAWNS, Assets.pawn, getCalculatedPosition());
  }

  @Override
  public void runAction(Action action) {}

  @Override
  public String details() {
    return "";
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

  private JobBoard jobboard;

  @Override
  protected void think() {
    super.think();
    
    // if we dont have a job
    if(!jobboard.workerHasJob(this)) {
      jobboard.requestJob(this); // try to get one
    }

    //      if we have a job           and need to go to it    and we're not pathing to it
    if(jobboard.workerHasJob(this) && !isAtJobStepLocation() && !isPathingToJobLocation()) {
      goToJobStepLocation(); // start pathing there.
      return; // and dont think about anything else.
    }

    // if we still dont have a job and we're not moving around
    if(!jobboard.workerHasJob(this) && !isPathing()) {
      if(Math.random() < 0.001f) wander(); // have a chance of wandering!
      return; // and dont think about anything else.
    }
  }

  private boolean isPathingToJobLocation() {
    if(!isPathing()) return false;
    return getDestination().isOneOf(jobboard.getJob(this).getCurrentStep().getLocations());
  }

  private boolean isAtJobStepLocation() {
    return this.getWorldPosition().asInt().isOneOf(jobboard.getJob(this).getCurrentStep().getLocations());
  }

  private void goToJobStepLocation() {
    goToClosest(jobboard
      .getJob(this)
      .getCurrentStep()
      .getLocations()
    );
  }

  // TODO at some point rewrite this to use an actor component array
  // where we loop through until something _does_ sometihng.
  @Override
  protected boolean act() {
    if(super.act()) return true;
    if(doJob()) return true;
    return false;
  }

  private boolean doJob() {
    if(!jobboard.workerHasJob(this)) return false;
    JobStep step = jobboard.getJob(this).getCurrentStep();
    // if we're not at the location of the job...
    if(!isAtJobStepLocation()) return false;

    if(step instanceof Job.Work) {
      Job.Work workStep = (Job.Work)step;
      if(workStep.doWork()) step.next();
      return true;
    }

    return false;
  }
}
