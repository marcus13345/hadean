package xyz.valnet.hadean.designation;

import xyz.valnet.hadean.gameobjects.worldobjects.Tree;

public class CutTreesDesignation extends Designation<Tree> {

  @Override
  protected Class<Tree> getType() {
    return Tree.class;
  }

  @Override
  protected void designate(Tree thing) {
    thing.runAction(Tree.ACTION_CHOP);
  }

  @Override
  public String getBuildTabName() {
    return "ChopTrees";
  }
}
