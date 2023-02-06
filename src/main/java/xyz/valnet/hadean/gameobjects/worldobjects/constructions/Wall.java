package xyz.valnet.hadean.gameobjects.worldobjects.constructions;

import java.util.EnumSet;

import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.graphics.Tile16.Direction;
import xyz.valnet.engine.math.Vector2i;
import xyz.valnet.hadean.gameobjects.terrain.Tile;
import xyz.valnet.hadean.gameobjects.worldobjects.items.Boulder;
import xyz.valnet.hadean.interfaces.BuildType;
import xyz.valnet.hadean.interfaces.IItemPredicate;
import xyz.valnet.hadean.interfaces.IPingable;
import xyz.valnet.hadean.util.Action;
import xyz.valnet.hadean.util.Assets;

public class Wall extends Construction implements IPingable {

  @Override
  protected void start() {
    super.start();
    ping();
  }

  @Override
  public String getName() {
    return "Wall";
  }

  @Override
  public Action[] getActions() {
    return new Action[0];
  }

  @Override
  public void runAction(Action action) {

  }

  @Override
  public boolean isWalkable() {
    return !isBuilt();
  }

  @Override
  public boolean shouldRemove() {
    return false;
  }

  @Override
  public void onRemove() {
    
  }

  private EnumSet<Direction> wallSides = EnumSet.noneOf(Direction.class);

  @Override
  public void ping() {
    Vector2i pos = getWorldBox().a.asInt();
    wallSides = EnumSet.noneOf(Direction.class);
    
    Tile north = terrain.getTile(pos.x, pos.y - 1);
    if(north != null && north.has(Wall.class)) wallSides.add(Direction.NORTH);
    
    Tile east = terrain.getTile(pos.x - 1, pos.y);
    if(east != null && east.has(Wall.class)) wallSides.add(Direction.EAST);
    
    Tile south = terrain.getTile(pos.x, pos.y + 1);
    if(south != null && south.has(Wall.class)) wallSides.add(Direction.SOUTH);
    
    Tile west = terrain.getTile(pos.x + 1, pos.y);
    if(west != null && west.has(Wall.class)) wallSides.add(Direction.WEST);
  }

  @Override
  public String getBuildTabCategory() {
    return "Structure";
  }

  @Override
  public BuildType getBuildType() {
    return BuildType.LINE;
  }

  @Override
  protected IItemPredicate getBuildingMaterial() {
    return Boulder.BOULDER_PREDICATE;
  }

  @Override
  protected int getBuildingMaterialCount() {
    return 1;
  }

  @Override
  protected Sprite getDefaultSprite() {
    return Assets.wall.getTextureFor(EnumSet.noneOf(Direction.class));
  }

  @Override
  protected float getMaxWork() {
    return super.getMaxWork();
  }
}
