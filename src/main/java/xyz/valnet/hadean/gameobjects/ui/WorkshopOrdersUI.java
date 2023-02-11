package xyz.valnet.hadean.gameobjects.ui;

import xyz.valnet.engine.graphics.ImmediateUI;
import xyz.valnet.engine.scenegraph.ITransient;
import xyz.valnet.hadean.gameobjects.inputlayer.SelectionLayer;
import xyz.valnet.hadean.interfaces.IWorkshop;

public class WorkshopOrdersUI extends ImmediateUI implements ITransient {

  private IWorkshop shop;
  private SelectionLayer selectionLayer;

  public void open(IWorkshop shop) {
    this.shop = shop;
  }

  public void close() {
    this.shop = null;
  }
  
  @Override
  protected void gui() {
    if(shop == null) return;
    
    window(0, 0, 200, 300, () -> {
      text("stuff");
    });
  }

  @Override
  protected void connect() {
    super.connect();
    this.selectionLayer = get(SelectionLayer.class);
  }

  @Override
  protected void start() {
    super.start();
    this.selectionLayer.subscribe((selection) -> {
      if(!selection.contains(shop)) {
        close();
      }
    });
  }
  
}
