package xyz.valnet.hadean.gameobjects.ui.tabs;

import xyz.valnet.hadean.gameobjects.BottomBar;
import xyz.valnet.hadean.gameobjects.JobBoard;

public class JobBoardTab extends Tab {
  
  private JobBoard jobBoard;

  private int height = 200;

  @Override
  protected void gui() {
    if(!shouldRender()) return;

    window(0, animate(576 + 50, 576 - BottomBar.bottomBarHeight - height + 1), 1024, height, () -> {
      horizontal(() -> {
        vertical(() -> {
          text("Valid");
          text(jobBoard.getValidJobs());
        });
        space(32);
        vertical(() -> {
          text("Invalid");
          text(jobBoard.getInvalidJobs());
        });
        space(32);
        vertical(() -> {
          text("Taken");
          text(jobBoard.getTakenJobs());
        });
      });

    });
  }

  @Override
  protected void connect() {
    super.connect();
    jobBoard = get(JobBoard.class);
  }

  @Override
  public String getTabName() {
    return "Work";
  }

  @Override
  protected void onClose() { }

  @Override
  protected void onOpen() { }
}
