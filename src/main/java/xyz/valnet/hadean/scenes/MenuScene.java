package xyz.valnet.hadean.scenes;

import xyz.valnet.engine.math.Vector4f;
import xyz.valnet.engine.scenegraph.IScene;
import xyz.valnet.hadean.input.Button;
import xyz.valnet.hadean.input.IButtonListener;
import xyz.valnet.hadean.util.Assets;

import static xyz.valnet.hadean.HadeanGame.Hadean;

public class MenuScene implements IScene, IButtonListener {

  private Button btnNewGame = new Button(Assets.frame, "New Game", 50, 200, 128, 32);
  private Button btnLoadGame = new Button(Assets.frame, "Load Game", 50, 240, 128, 32);
  private Button btnOptions = new Button(Assets.frame, "Options", 50, 280, 128, 32);
  private Button btnQuit = new Button(Assets.frame, "Quit", 50, 320, 128, 32);

  public MenuScene() {
    btnNewGame.registerClickListener(this);
    btnLoadGame.registerClickListener(this);
    btnOptions.registerClickListener(this);
    btnQuit.registerClickListener(this);
  }

  public Vector4f green =  new Vector4f(0.0f, 1.0f, 0.2f, 1.0f);
  public Vector4f cyan =   new Vector4f(0.1f, 0.7f, 1.0f, 1.0f);
  public Vector4f yellow = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
  public Vector4f red =    new Vector4f(1.0f, 0.1f, 0.1f, 1.0f);

  @Override
  public void render() {
    Assets.flat.pushColor(green);
    btnNewGame.draw();
    Assets.flat.swapColor(cyan);
    btnLoadGame.draw();
    Assets.flat.swapColor(yellow);
    btnOptions.draw();
    Assets.flat.swapColor(red);
    btnQuit.draw();
    Assets.flat.popColor();
  }

  @Override
  public void update(float dTime) {
    btnNewGame.update();
    btnLoadGame.update();
    btnOptions.update();
    btnQuit.update();
  }

  @Override
  public void click(Button target) {
    if(target == btnNewGame) {
      newGame();
    } else if(target == btnQuit) {
      quit();
    }
  }

  private void newGame() {
    Hadean.changeScene(new GameScene());
  }

  private void quit() {

  }

  @Override
  public void enable() {
    
  }

  @Override
  public void disable() {
    
  }
  
}
