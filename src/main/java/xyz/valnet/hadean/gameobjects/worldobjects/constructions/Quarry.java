package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.jobs.Job;
import xyz.valnet.hadean.gameobjects.jobs.JobBoard;
import xyz.valnet.hadean.gameobjects.jobs.SimpleWorkable;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Boulder;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.util.Assets;

public class Quarry extends Construction {

  private Job digJob = null;

  @Override
  public Vector2i getDimensions() {
    return new Vector2i(3, 3);
  }

  private float digProgress = 0;

  private void tryCreateDigJob() {
    if(!isBuilt()) return;
    if (digJob != null) return;
    if (terrain.getTile(getWorldPosition().xy().south().east()).has(Boulder.class)) return;

    digJob = get(JobBoard.class).postSimpleWorkJob(new SimpleWorkable("Mine at Quarry", 5000, () -> {
      return new Vector2i[] {
        getWorldPosition().xy().south().east()
      };
    }, (progress) -> {
      digProgress = progress;
    }));

    digJob.registerClosedListener(() -> {
      digProgress = 0;
      Vector2i dropPos = getWorldPosition().xy().south().east();
      Boulder boulder = new Boulder(dropPos.x, dropPos.y);
      add(boulder);
      boulder.runAction(Boulder.HAUL);
      digJob = null;
    });
  }

  @Override
  public void update(float dTime) {
    super.update(dTime);
    tryCreateDigJob();
  }

  @Override
  public boolean isWalkable() {
    return true;
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {
  }

  @Override
  public String getName() {
    return "Quarry";
  }

  @Override
  protected IItemPredicate getBuildingMaterial() {
    return null;
  }

  @Override
  protected int getBuildingMaterialCount() {
    return 0;
  }

  @Override
  protected final Sprite getDefaultSprite() {
    return Assets.quarry;
  }

  @Override
  public void render() {
    super.render();
    if(!isBuilt()) return;

    if(digJob != null && !digJob.isCompleted() && digProgress > 0) {
      camera.drawProgressBar(digProgress, getWorldBox());
    }
  }
}
