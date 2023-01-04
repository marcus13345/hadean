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
  public static final Tile9 selectionFrame;
  public static final Tile9 selectedFrame;
  public static final Tile9 uiFrame;
  public static final Tile9 uiFrameLight;
  public static final Tile9 uiFrameDark;
  public static final Tile9 fillColor;
  
  
  public static final Sprite[] defaultTerrain;
  public static final Sprite[] growingRice;
  public static final Sprite[] farmPlot;
  public static final Sprite pawn;
  public static final Sprite tree;
  public static final Sprite rocks;
  public static final Sprite log;
  public static final Sprite lilAxe;
  public static final Sprite haulArrow;
  public static final Sprite checkerBoard;
  public static final Sprite riceBag;
  public static final Sprite whiteBox;
  public static final Sprite chicken;
  public static final Sprite bed;
  public static final Sprite egg;
  public static final Sprite bigRock;
  public static final Sprite lilPickaxe;
  public static final Sprite testTile;


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

    farmPlot = new Sprite[] {
      new Sprite(atlas, 24, 120, 8, 8),
      new Sprite(atlas, 32, 120, 8, 8),
      new Sprite(atlas, 24, 128, 8, 8),
      new Sprite(atlas, 32, 128, 8, 8)
    };

    pawn = new Sprite(atlas, 48, 88, 8, 8);
    tree = new Sprite(atlas, 64, 64, 24, 24);
    rocks = new Sprite(atlas, 64, 104, 8, 8);
    log = new Sprite(atlas, 48, 96, 16, 16);
    lilAxe = new Sprite(atlas, 64, 88, 16, 16);
    haulArrow = new Sprite(atlas, 80, 88, 16, 16);
    checkerBoard = new Sprite(atlas, 40, 64, 4, 4);
    riceBag = new Sprite(atlas, 48, 112, 16, 16);
    chicken = new Sprite(atlas, 0, 104, 8, 8);
    bed = new Sprite(atlas, 0, 120, 8, 16);
    egg = new Sprite(atlas, 8, 104, 8, 8);
    bigRock = new Sprite(atlas, 16, 104, 8, 8);
    lilPickaxe = new Sprite(atlas, 8, 120, 16, 16);
    testTile = new Sprite(atlas, 16, 16, 64, 112);

    Map<Character, Sprite> charset = new HashMap<Character, Sprite>();

    charset.put('A', new Sprite(atlas,   0, 0, 8, 16));
    charset.put('B', new Sprite(atlas,   8, 0, 8, 16));
    charset.put('C', new Sprite(atlas,  16, 0, 8, 16));
    charset.put('D', new Sprite(atlas,  24, 0, 8, 16));
    charset.put('E', new Sprite(atlas,  32, 0, 8, 16));
    charset.put('F', new Sprite(atlas,  40, 0, 8, 16));
    charset.put('G', new Sprite(atlas,  48, 0, 8, 16));
    charset.put('H', new Sprite(atlas,  56, 0, 8, 16));
    charset.put('I', new Sprite(atlas,  64, 0, 8, 16));
    charset.put('J', new Sprite(atlas,  72, 0, 8, 16));
    charset.put('K', new Sprite(atlas,  80, 0, 8, 16));
    charset.put('L', new Sprite(atlas,  88, 0, 8, 16));
    charset.put('M', new Sprite(atlas,  96, 0, 8, 16));
    charset.put('N', new Sprite(atlas, 104, 0, 8, 16));
    charset.put('O', new Sprite(atlas, 112, 0, 8, 16));
    charset.put('P', new Sprite(atlas, 120, 0, 8, 16));
    charset.put('Q', new Sprite(atlas, 128, 0, 8, 16));
    charset.put('R', new Sprite(atlas, 136, 0, 8, 16));
    charset.put('S', new Sprite(atlas, 144, 0, 8, 16));
    charset.put('T', new Sprite(atlas, 152, 0, 8, 16));
    charset.put('U', new Sprite(atlas, 160, 0, 8, 16));
    charset.put('V', new Sprite(atlas, 168, 0, 8, 16));
    charset.put('W', new Sprite(atlas, 176, 0, 8, 16));
    charset.put('X', new Sprite(atlas, 184, 0, 8, 16));
    charset.put('Y', new Sprite(atlas, 192, 0, 8, 16));
    charset.put('Z', new Sprite(atlas, 200, 0, 8, 16));
    charset.put(':', new Sprite(atlas, 208, 0, 8, 16));
    charset.put(';', new Sprite(atlas, 216, 0, 8, 16));
    charset.put('.', new Sprite(atlas, 224, 0, 8, 16));
    charset.put(',', new Sprite(atlas, 232, 0, 8, 16));
    charset.put('!', new Sprite(atlas, 240, 0, 8, 16));
    charset.put('?', new Sprite(atlas, 248, 0, 8, 16));
    
    charset.put('a',  new Sprite(atlas,   0, 16, 8, 16));
    charset.put('b',  new Sprite(atlas,   8, 16, 8, 16));
    charset.put('c',  new Sprite(atlas,  16, 16, 8, 16));
    charset.put('d',  new Sprite(atlas,  24, 16, 8, 16));
    charset.put('e',  new Sprite(atlas,  32, 16, 8, 16));
    charset.put('f',  new Sprite(atlas,  40, 16, 8, 16));
    charset.put('g',  new Sprite(atlas,  48, 16, 8, 16));
    charset.put('h',  new Sprite(atlas,  56, 16, 8, 16));
    charset.put('i',  new Sprite(atlas,  64, 16, 8, 16));
    charset.put('j',  new Sprite(atlas,  72, 16, 8, 16));
    charset.put('k',  new Sprite(atlas,  80, 16, 8, 16));
    charset.put('l',  new Sprite(atlas,  88, 16, 8, 16));
    charset.put('m',  new Sprite(atlas,  96, 16, 8, 16));
    charset.put('n',  new Sprite(atlas, 104, 16, 8, 16));
    charset.put('o',  new Sprite(atlas, 112, 16, 8, 16));
    charset.put('p',  new Sprite(atlas, 120, 16, 8, 16));
    charset.put('q',  new Sprite(atlas, 128, 16, 8, 16));
    charset.put('r',  new Sprite(atlas, 136, 16, 8, 16));
    charset.put('s',  new Sprite(atlas, 144, 16, 8, 16));
    charset.put('t',  new Sprite(atlas, 152, 16, 8, 16));
    charset.put('u',  new Sprite(atlas, 160, 16, 8, 16));
    charset.put('v',  new Sprite(atlas, 168, 16, 8, 16));
    charset.put('w',  new Sprite(atlas, 176, 16, 8, 16));
    charset.put('x',  new Sprite(atlas, 184, 16, 8, 16));
    charset.put('y',  new Sprite(atlas, 192, 16, 8, 16));
    charset.put('z',  new Sprite(atlas, 200, 16, 8, 16));
    charset.put('\'', new Sprite(atlas, 208, 16, 8, 16));
    charset.put('"',  new Sprite(atlas, 216, 16, 8, 16));
    charset.put('[',  new Sprite(atlas, 224, 16, 8, 16));
    charset.put(']',  new Sprite(atlas, 232, 16, 8, 16));
    charset.put('{',  new Sprite(atlas, 240, 16, 8, 16));
    charset.put('}',  new Sprite(atlas, 248, 16, 8, 16));
    
    charset.put('0',  new Sprite(atlas,   0, 32, 8, 16));
    charset.put('1',  new Sprite(atlas,   8, 32, 8, 16));
    charset.put('2',  new Sprite(atlas,  16, 32, 8, 16));
    charset.put('3',  new Sprite(atlas,  24, 32, 8, 16));
    charset.put('4',  new Sprite(atlas,  32, 32, 8, 16));
    charset.put('5',  new Sprite(atlas,  40, 32, 8, 16));
    charset.put('6',  new Sprite(atlas,  48, 32, 8, 16));
    charset.put('7',  new Sprite(atlas,  56, 32, 8, 16));
    charset.put('8',  new Sprite(atlas,  64, 32, 8, 16));
    charset.put('9',  new Sprite(atlas,  72, 32, 8, 16));
    charset.put('@',  new Sprite(atlas,  80, 32, 8, 16));
    charset.put('#',  new Sprite(atlas,  88, 32, 8, 16));
    charset.put('$',  new Sprite(atlas,  96, 32, 8, 16));
    charset.put('%',  new Sprite(atlas, 104, 32, 8, 16));
    charset.put('^',  new Sprite(atlas, 112, 32, 8, 16));
    charset.put('&',  new Sprite(atlas, 120, 32, 8, 16));
    charset.put('*',  new Sprite(atlas, 128, 32, 8, 16));
    charset.put('(',  new Sprite(atlas, 136, 32, 8, 16));
    charset.put(')',  new Sprite(atlas, 144, 32, 8, 16));
    charset.put('<',  new Sprite(atlas, 152, 32, 8, 16));
    charset.put('>',  new Sprite(atlas, 160, 32, 8, 16));
    charset.put('_',  new Sprite(atlas, 168, 32, 8, 16));
    charset.put('-',  new Sprite(atlas, 176, 32, 8, 16));
    charset.put('+',  new Sprite(atlas, 184, 32, 8, 16));
    charset.put('=',  new Sprite(atlas, 192, 32, 8, 16));
    charset.put('/',  new Sprite(atlas, 200, 32, 8, 16));
    charset.put('\\', new Sprite(atlas, 208, 32, 8, 16));
    charset.put('♥',  new Sprite(atlas, 216, 32, 8, 16));
    charset.put('|',  new Sprite(atlas, 224, 32, 8, 16));
    font = new Font(charset, 8, 16);

    Map<Character, Sprite> miniCharset = new HashMap<Character, Sprite>();
    miniCharset.put('1', new Sprite(atlas, 0,  112, 4, 5));
    miniCharset.put('2', new Sprite(atlas, 4,  112, 4, 5));
    miniCharset.put('3', new Sprite(atlas, 8,  112, 4, 5));
    miniCharset.put('4', new Sprite(atlas, 12, 112, 4, 5));
    miniCharset.put('5', new Sprite(atlas, 16, 112, 4, 5));
    miniCharset.put('6', new Sprite(atlas, 20, 112, 4, 5));
    miniCharset.put('7', new Sprite(atlas, 24, 112, 4, 5));
    miniCharset.put('8', new Sprite(atlas, 28, 112, 4, 5));
    miniCharset.put('9', new Sprite(atlas, 32, 112, 4, 5));
    miniCharset.put('0', new Sprite(atlas, 36, 112, 4, 5));
    miniFont = new Font(miniCharset, 4, 5);

    frame = new Tile9(
      new Sprite(atlas, 24,  88, 8, 8),
      new Sprite(atlas, 32,  88, 8, 8),
      new Sprite(atlas, 40,  88, 8, 8),
      new Sprite(atlas, 24,  96, 8, 8),
      new Sprite(atlas, 32,  96, 8, 8),
      new Sprite(atlas, 40,  96, 8, 8),
      new Sprite(atlas, 24, 104, 8, 8),
      new Sprite(atlas, 32, 104, 8, 8),
      new Sprite(atlas, 40, 104, 8, 8)
    );

    redFrame = new Tile9(
      new Sprite(atlas,  0,  88, 8, 8),
      new Sprite(atlas,  8,  88, 8, 8),
      new Sprite(atlas, 16,  88, 8, 8),
      new Sprite(atlas,  0,  96, 8, 8),
      new Sprite(atlas,  8,  96, 8, 8),
      new Sprite(atlas, 16,  96, 8, 8),
      new Sprite(atlas,  0, 104, 8, 8),
      new Sprite(atlas,  8, 104, 8, 8),
      new Sprite(atlas, 16, 104, 8, 8)
    );

    selectionFrame = new Tile9(
      new Sprite(atlas, 56, 88, 3, 3),
      new Sprite(atlas, 59, 88, 2, 3),
      new Sprite(atlas, 61, 88, 3, 3),
      new Sprite(atlas, 56, 91, 3, 2),
      new Sprite(atlas, 59, 91, 2, 2),
      new Sprite(atlas, 61, 91, 3, 2),
      new Sprite(atlas, 56, 93, 3, 3),
      new Sprite(atlas, 59, 93, 2, 3),
      new Sprite(atlas, 61, 93, 3, 3)
    );
    
    selectedFrame = new Tile9(
      new Sprite(atlas,  8, 88, 7, 7),
      new Sprite(atlas, 15, 88, 2, 7),
      new Sprite(atlas, 17, 88, 7, 7),
      new Sprite(atlas,  8, 95, 7, 2),
      new Sprite(atlas, 15, 95, 2, 2),
      new Sprite(atlas, 17, 95, 7, 2),
      new Sprite(atlas,  8, 97, 7, 7),
      new Sprite(atlas, 15, 97, 2, 7),
      new Sprite(atlas, 17, 97, 7, 7)
    );

    fillColor = new Tile9(
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1),
      new Sprite(atlas, 44, 64, 1, 1)
    );
    
    uiFrame = new Tile9(
      new Sprite(atlas, 32, 80, 1, 1),
      new Sprite(atlas, 33, 80, 6, 1),
      new Sprite(atlas, 39, 80, 1, 1),
      new Sprite(atlas, 32, 81, 1, 6),
      new Sprite(atlas, 33, 81, 6, 6),
      new Sprite(atlas, 39, 81, 1, 6),
      new Sprite(atlas, 32, 87, 1, 1),
      new Sprite(atlas, 33, 87, 6, 1),
      new Sprite(atlas, 39, 87, 1, 1)
    );
    
    uiFrameLight = new Tile9(
      new Sprite(atlas, 24, 80, 1, 1),
      new Sprite(atlas, 25, 80, 6, 1),
      new Sprite(atlas, 31, 80, 1, 1),
      new Sprite(atlas, 24, 81, 1, 6),
      new Sprite(atlas, 25, 81, 6, 6),
      new Sprite(atlas, 31, 81, 1, 6),
      new Sprite(atlas, 24, 87, 1, 1),
      new Sprite(atlas, 25, 87, 6, 1),
      new Sprite(atlas, 31, 87, 1, 1)
    );
    
    uiFrameDark = new Tile9(
      new Sprite(atlas, 0,  96, 1, 1),
      new Sprite(atlas, 1,  96, 6, 1),
      new Sprite(atlas, 7,  96, 1, 1),
      new Sprite(atlas, 0,  97, 1, 6),
      new Sprite(atlas, 1,  97, 6, 6),
      new Sprite(atlas, 7,  97, 1, 6),
      new Sprite(atlas, 0, 103, 1, 1),
      new Sprite(atlas, 1, 103, 6, 1),
      new Sprite(atlas, 7, 103, 1, 1)
    );

  }
}
