package xyz.valnet.hadean.designation;

import xyz.valnet.hadean.gameobjects.worldobjects.Tree;
import xyz.valnet.hadean.interfaces.BuildableMetadata;

@BuildableMetadata(category = "Designations", name = "Chop Trees")
public class CutTreesDesignation extends Designation<Tree> {

  @Override
  protected Class<Tree> getType() {
    return Tree.class;
  }

  @Override
  protected void designate(Tree thing) {
    thing.runAction(Tree.ACTION_CHOP);
  }
  
}
