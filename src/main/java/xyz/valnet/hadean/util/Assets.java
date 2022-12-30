package xyz.valnet.hadean.util;

import java.util.HashMap;
import java.util.Map;

import xyz.valnet.engine.graphics.Font;
import xyz.valnet.engine.graphics.Sprite;
import xyz.valnet.engine.graphics.Texture;
import xyz.valnet.engine.graphics.Tile9;
import xyz.valnet.engine.math.Vector4i;
import xyz.valnet.engine.shaders.SimpleShader;

public class Assets {

  public static final Texture atlas;
  public static final Font font;
  public static final Font miniFont;
  public static final Tile9 redFrame;
  public static final Tile9 frame;
  public static final Tile9 fireFrame;
  public static final Tile9 selectionFrame;
  public static final Tile9 selectedFrame;
  public static final Tile9 uiFrame;
  public static final Tile9 uiFrameLight;
  public static final Tile9 uiFrameDark;
  
  public static final Sprite[] defaultTerrain;
  public static final Sprite[] growingRice;
  public static final Sprite pawn;
  public static final Sprite tree;
  public static final Sprite rocks;
  public static final Sprite log;
  public static final Sprite lilAxe;
  public static final Sprite haulArrow;
  public static final Sprite checkerBoard;
  public static final Sprite riceBag;
  public static final Sprite farmPlot;
  public static final Sprite whiteBox;
  public static final Sprite chicken;
  public static final Sprite bed;
  public static final Sprite egg;
  public static final Sprite bigRock;
  public static final Sprite lilPickaxe;


  public static final SimpleShader flat;

  static {
    flat = new SimpleShader("shaders/flat.vert", "shaders/flat.frag");

    atlas = new Texture("res/textures.png");

    whiteBox = new Sprite(atlas, 0, 88, 8, 8);

    defaultTerrain = new Sprite[] {
      new Sprite(atlas, new Vector4i(24, 72, 8, 8)),
      new Sprite(atlas, new Vector4i(24, 64, 8, 8)),
      new Sprite(atlas, new Vector4i(32, 72, 8, 8)),
      new Sprite(atlas, new Vector4i(32, 64, 8, 8))
    };

    growingRice = new Sprite[] {
      new Sprite(atlas, new Vector4i(40, 80, 8, 8)),
      new Sprite(atlas, new Vector4i(48, 80, 8, 8)),
      new Sprite(atlas, new Vector4i(48, 64, 8, 16)),
      new Sprite(atlas, new Vector4i(56, 64, 8, 16))
    };

    pawn = new Sprite(atlas, 48, 88, 8, 8);
    tree = new Sprite(atlas, 64, 64, 24, 24);
    rocks = new Sprite(atlas, 64, 104, 8, 8);
    log = new Sprite(atlas, 48, 96, 16, 16);
    lilAxe = new Sprite(atlas, 64, 88, 16, 16);
    haulArrow = new Sprite(atlas, 80, 88, 16, 16);
    checkerBoard = new Sprite(atlas, 40, 64, 4, 4);
    riceBag = new Sprite(atlas, 48, 112, 16, 16);
    farmPlot = new Sprite(atlas, 40, 72, 8, 8);
    chicken = new Sprite(atlas, 0, 104, 8, 8);
    bed = new Sprite(atlas, 0, 120, 8, 16);
    egg = new Sprite(atlas, 8, 104, 8, 8);
    bigRock = new Sprite(atlas, 16, 104, 8, 8);
    lilPickaxe = new Sprite(atlas, 8, 120, 16, 16)

    Map<Character, Sprite> charset = new HashMap<Character, Sprite>();

    charset.put('A', new Sprite(atlas, new Vector4i(  0, 0, 8, 16)));
    charset.put('B', new Sprite(atlas, new Vector4i(  8, 0, 8, 16)));
    charset.put('C', new Sprite(atlas, new Vector4i( 16, 0, 8, 16)));
    charset.put('D', new Sprite(atlas, new Vector4i( 24, 0, 8, 16)));
    charset.put('E', new Sprite(atlas, new Vector4i( 32, 0, 8, 16)));
    charset.put('F', new Sprite(atlas, new Vector4i( 40, 0, 8, 16)));
    charset.put('G', new Sprite(atlas, new Vector4i( 48, 0, 8, 16)));
    charset.put('H', new Sprite(atlas, new Vector4i( 56, 0, 8, 16)));
    charset.put('I', new Sprite(atlas, new Vector4i( 64, 0, 8, 16)));
    charset.put('J', new Sprite(atlas, new Vector4i( 72, 0, 8, 16)));
    charset.put('K', new Sprite(atlas, new Vector4i( 80, 0, 8, 16)));
    charset.put('L', new Sprite(atlas, new Vector4i( 88, 0, 8, 16)));
    charset.put('M', new Sprite(atlas, new Vector4i( 96, 0, 8, 16)));
    charset.put('N', new Sprite(atlas, new Vector4i(104, 0, 8, 16)));
    charset.put('O', new Sprite(atlas, new Vector4i(112, 0, 8, 16)));
    charset.put('P', new Sprite(atlas, new Vector4i(120, 0, 8, 16)));
    charset.put('Q', new Sprite(atlas, new Vector4i(128, 0, 8, 16)));
    charset.put('R', new Sprite(atlas, new Vector4i(136, 0, 8, 16)));
    charset.put('S', new Sprite(atlas, new Vector4i(144, 0, 8, 16)));
    charset.put('T', new Sprite(atlas, new Vector4i(152, 0, 8, 16)));
    charset.put('U', new Sprite(atlas, new Vector4i(160, 0, 8, 16)));
    charset.put('V', new Sprite(atlas, new Vector4i(168, 0, 8, 16)));
    charset.put('W', new Sprite(atlas, new Vector4i(176, 0, 8, 16)));
    charset.put('X', new Sprite(atlas, new Vector4i(184, 0, 8, 16)));
    charset.put('Y', new Sprite(atlas, new Vector4i(192, 0, 8, 16)));
    charset.put('Z', new Sprite(atlas, new Vector4i(200, 0, 8, 16)));
    charset.put(':', new Sprite(atlas, new Vector4i(208, 0, 8, 16)));
    charset.put(';', new Sprite(atlas, new Vector4i(216, 0, 8, 16)));
    charset.put('.', new Sprite(atlas, new Vector4i(224, 0, 8, 16)));
    charset.put(',', new Sprite(atlas, new Vector4i(232, 0, 8, 16)));
    charset.put('!', new Sprite(atlas, new Vector4i(240, 0, 8, 16)));
    charset.put('?', new Sprite(atlas, new Vector4i(248, 0, 8, 16)));
    
    charset.put('a',  new Sprite(atlas, new Vector4i(  0, 16, 8, 16)));
    charset.put('b',  new Sprite(atlas, new Vector4i(  8, 16, 8, 16)));
    charset.put('c',  new Sprite(atlas, new Vector4i( 16, 16, 8, 16)));
    charset.put('d',  new Sprite(atlas, new Vector4i( 24, 16, 8, 16)));
    charset.put('e',  new Sprite(atlas, new Vector4i( 32, 16, 8, 16)));
    charset.put('f',  new Sprite(atlas, new Vector4i( 40, 16, 8, 16)));
    charset.put('g',  new Sprite(atlas, new Vector4i( 48, 16, 8, 16)));
    charset.put('h',  new Sprite(atlas, new Vector4i( 56, 16, 8, 16)));
    charset.put('i',  new Sprite(atlas, new Vector4i( 64, 16, 8, 16)));
    charset.put('j',  new Sprite(atlas, new Vector4i( 72, 16, 8, 16)));
    charset.put('k',  new Sprite(atlas, new Vector4i( 80, 16, 8, 16)));
    charset.put('l',  new Sprite(atlas, new Vector4i( 88, 16, 8, 16)));
    charset.put('m',  new Sprite(atlas, new Vector4i( 96, 16, 8, 16)));
    charset.put('n',  new Sprite(atlas, new Vector4i(104, 16, 8, 16)));
    charset.put('o',  new Sprite(atlas, new Vector4i(112, 16, 8, 16)));
    charset.put('p',  new Sprite(atlas, new Vector4i(120, 16, 8, 16)));
    charset.put('q',  new Sprite(atlas, new Vector4i(128, 16, 8, 16)));
    charset.put('r',  new Sprite(atlas, new Vector4i(136, 16, 8, 16)));
    charset.put('s',  new Sprite(atlas, new Vector4i(144, 16, 8, 16)));
    charset.put('t',  new Sprite(atlas, new Vector4i(152, 16, 8, 16)));
    charset.put('u',  new Sprite(atlas, new Vector4i(160, 16, 8, 16)));
    charset.put('v',  new Sprite(atlas, new Vector4i(168, 16, 8, 16)));
    charset.put('w',  new Sprite(atlas, new Vector4i(176, 16, 8, 16)));
    charset.put('x',  new Sprite(atlas, new Vector4i(184, 16, 8, 16)));
    charset.put('y',  new Sprite(atlas, new Vector4i(192, 16, 8, 16)));
    charset.put('z',  new Sprite(atlas, new Vector4i(200, 16, 8, 16)));
    charset.put('\'', new Sprite(atlas, new Vector4i(208, 16, 8, 16)));
    charset.put('"',  new Sprite(atlas, new Vector4i(216, 16, 8, 16)));
    charset.put('[',  new Sprite(atlas, new Vector4i(224, 16, 8, 16)));
    charset.put(']',  new Sprite(atlas, new Vector4i(232, 16, 8, 16)));
    charset.put('{',  new Sprite(atlas, new Vector4i(240, 16, 8, 16)));
    charset.put('}',  new Sprite(atlas, new Vector4i(248, 16, 8, 16)));
    
    charset.put('0',  new Sprite(atlas, new Vector4i(  0, 32, 8, 16)));
    charset.put('1',  new Sprite(atlas, new Vector4i(  8, 32, 8, 16)));
    charset.put('2',  new Sprite(atlas, new Vector4i( 16, 32, 8, 16)));
    charset.put('3',  new Sprite(atlas, new Vector4i( 24, 32, 8, 16)));
    charset.put('4',  new Sprite(atlas, new Vector4i( 32, 32, 8, 16)));
    charset.put('5',  new Sprite(atlas, new Vector4i( 40, 32, 8, 16)));
    charset.put('6',  new Sprite(atlas, new Vector4i( 48, 32, 8, 16)));
    charset.put('7',  new Sprite(atlas, new Vector4i( 56, 32, 8, 16)));
    charset.put('8',  new Sprite(atlas, new Vector4i( 64, 32, 8, 16)));
    charset.put('9',  new Sprite(atlas, new Vector4i( 72, 32, 8, 16)));
    charset.put('@',  new Sprite(atlas, new Vector4i( 80, 32, 8, 16)));
    charset.put('#',  new Sprite(atlas, new Vector4i( 88, 32, 8, 16)));
    charset.put('$',  new Sprite(atlas, new Vector4i( 96, 32, 8, 16)));
    charset.put('%',  new Sprite(atlas, new Vector4i(104, 32, 8, 16)));
    charset.put('^',  new Sprite(atlas, new Vector4i(112, 32, 8, 16)));
    charset.put('&',  new Sprite(atlas, new Vector4i(120, 32, 8, 16)));
    charset.put('*',  new Sprite(atlas, new Vector4i(128, 32, 8, 16)));
    charset.put('(',  new Sprite(atlas, new Vector4i(136, 32, 8, 16)));
    charset.put(')',  new Sprite(atlas, new Vector4i(144, 32, 8, 16)));
    charset.put('<',  new Sprite(atlas, new Vector4i(152, 32, 8, 16)));
    charset.put('>',  new Sprite(atlas, new Vector4i(160, 32, 8, 16)));
    charset.put('_',  new Sprite(atlas, new Vector4i(168, 32, 8, 16)));
    charset.put('-',  new Sprite(atlas, new Vector4i(176, 32, 8, 16)));
    charset.put('+',  new Sprite(atlas, new Vector4i(184, 32, 8, 16)));
    charset.put('=',  new Sprite(atlas, new Vector4i(192, 32, 8, 16)));
    charset.put('/',  new Sprite(atlas, new Vector4i(200, 32, 8, 16)));
    charset.put('\\', new Sprite(atlas, new Vector4i(208, 32, 8, 16)));
    charset.put('♥',  new Sprite(atlas, new Vector4i(216, 32, 8, 16)));
    charset.put('|',  new Sprite(atlas, new Vector4i(224, 32, 8, 16)));
    font = new Font(charset, 8, 16);

    Map<Character, Sprite> miniCharset = new HashMap<Character, Sprite>();
    miniCharset.put('1', new Sprite(atlas, new Vector4i(0,  112, 4, 5)));
    miniCharset.put('2', new Sprite(atlas, new Vector4i(4,  112, 4, 5)));
    miniCharset.put('3', new Sprite(atlas, new Vector4i(8,  112, 4, 5)));
    miniCharset.put('4', new Sprite(atlas, new Vector4i(12, 112, 4, 5)));
    miniCharset.put('5', new Sprite(atlas, new Vector4i(16, 112, 4, 5)));
    miniCharset.put('6', new Sprite(atlas, new Vector4i(20, 112, 4, 5)));
    miniCharset.put('7', new Sprite(atlas, new Vector4i(24, 112, 4, 5)));
    miniCharset.put('8', new Sprite(atlas, new Vector4i(28, 112, 4, 5)));
    miniCharset.put('9', new Sprite(atlas, new Vector4i(32, 112, 4, 5)));
    miniCharset.put('0', new Sprite(atlas, new Vector4i(36, 112, 4, 5)));
    miniFont = new Font(miniCharset, 4, 5);

    frame = new Tile9(
      new Sprite(atlas, new Vector4i(24,  88, 8, 8)),
      new Sprite(atlas, new Vector4i(32,  88, 8, 8)),
      new Sprite(atlas, new Vector4i(40,  88, 8, 8)),
      new Sprite(atlas, new Vector4i(24,  96, 8, 8)),
      new Sprite(atlas, new Vector4i(32,  96, 8, 8)),
      new Sprite(atlas, new Vector4i(40,  96, 8, 8)),
      new Sprite(atlas, new Vector4i(24, 104, 8, 8)),
      new Sprite(atlas, new Vector4i(32, 104, 8, 8)),
      new Sprite(atlas, new Vector4i(40, 104, 8, 8))
    );

    redFrame = new Tile9(
      new Sprite(atlas, new Vector4i( 0,  88, 8, 8)),
      new Sprite(atlas, new Vector4i( 8,  88, 8, 8)),
      new Sprite(atlas, new Vector4i(16,  88, 8, 8)),
      new Sprite(atlas, new Vector4i( 0,  96, 8, 8)),
      new Sprite(atlas, new Vector4i( 8,  96, 8, 8)),
      new Sprite(atlas, new Vector4i(16,  96, 8, 8)),
      new Sprite(atlas, new Vector4i( 0, 104, 8, 8)),
      new Sprite(atlas, new Vector4i( 8, 104, 8, 8)),
      new Sprite(atlas, new Vector4i(16, 104, 8, 8))
    );

    fireFrame = new Tile9(
      new Sprite(atlas, new Vector4i( 0,  88 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i( 8,  88 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i(16,  88 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i( 0,  96 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i( 8,  96 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i(16,  96 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i( 0, 104 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i( 8, 104 - 24, 8, 8)),
      new Sprite(atlas, new Vector4i(16, 104 - 24, 8, 8))
    );

    selectionFrame = new Tile9(
      new Sprite(atlas, new Vector4i(56, 88, 3, 3)),
      new Sprite(atlas, new Vector4i(59, 88, 2, 3)),
      new Sprite(atlas, new Vector4i(61, 88, 3, 3)),
      new Sprite(atlas, new Vector4i(56, 91, 3, 2)),
      new Sprite(atlas, new Vector4i(59, 91, 2, 2)),
      new Sprite(atlas, new Vector4i(61, 91, 3, 2)),
      new Sprite(atlas, new Vector4i(56, 93, 3, 3)),
      new Sprite(atlas, new Vector4i(59, 93, 2, 3)),
      new Sprite(atlas, new Vector4i(61, 93, 3, 3))
    );
    
    selectedFrame = new Tile9(
      new Sprite(atlas, new Vector4i( 8, 88, 7, 7)),
      new Sprite(atlas, new Vector4i(15, 88, 2, 7)),
      new Sprite(atlas, new Vector4i(17, 88, 7, 7)),
      new Sprite(atlas, new Vector4i( 8, 95, 7, 2)),
      new Sprite(atlas, new Vector4i(15, 95, 2, 2)),
      new Sprite(atlas, new Vector4i(17, 95, 7, 2)),
      new Sprite(atlas, new Vector4i( 8, 97, 7, 7)),
      new Sprite(atlas, new Vector4i(15, 97, 2, 7)),
      new Sprite(atlas, new Vector4i(17, 97, 7, 7))
    );
    
    uiFrame = new Tile9(
      new Sprite(atlas, new Vector4i(32, 80, 1, 1)),
      new Sprite(atlas, new Vector4i(33, 80, 6, 1)),
      new Sprite(atlas, new Vector4i(39, 80, 1, 1)),
      new Sprite(atlas, new Vector4i(32, 81, 1, 6)),
      new Sprite(atlas, new Vector4i(33, 81, 6, 6)),
      new Sprite(atlas, new Vector4i(39, 81, 1, 6)),
      new Sprite(atlas, new Vector4i(32, 87, 1, 1)),
      new Sprite(atlas, new Vector4i(33, 87, 6, 1)),
      new Sprite(atlas, new Vector4i(39, 87, 1, 1))
    );
    
    uiFrameLight = new Tile9(
      new Sprite(atlas, new Vector4i(24, 80, 1, 1)),
      new Sprite(atlas, new Vector4i(25, 80, 6, 1)),
      new Sprite(atlas, new Vector4i(31, 80, 1, 1)),
      new Sprite(atlas, new Vector4i(24, 81, 1, 6)),
      new Sprite(atlas, new Vector4i(25, 81, 6, 6)),
      new Sprite(atlas, new Vector4i(31, 81, 1, 6)),
      new Sprite(atlas, new Vector4i(24, 87, 1, 1)),
      new Sprite(atlas, new Vector4i(25, 87, 6, 1)),
      new Sprite(atlas, new Vector4i(31, 87, 1, 1))
    );
    
    uiFrameDark = new Tile9(
      new Sprite(atlas, new Vector4i(0,  96, 1, 1)),
      new Sprite(atlas, new Vector4i(1,  96, 6, 1)),
      new Sprite(atlas, new Vector4i(7,  96, 1, 1)),
      new Sprite(atlas, new Vector4i(0,  97, 1, 6)),
      new Sprite(atlas, new Vector4i(1,  97, 6, 6)),
      new Sprite(atlas, new Vector4i(7,  97, 1, 6)),
      new Sprite(atlas, new Vector4i(0, 103, 1, 1)),
      new Sprite(atlas, new Vector4i(1, 103, 6, 1)),
      new Sprite(atlas, new Vector4i(7, 103, 1, 1))
    );

  }
}
