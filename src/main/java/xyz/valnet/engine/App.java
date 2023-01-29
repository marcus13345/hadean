package xyz.valnet.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import xyz.valnet.engine.math.Matrix4f;
import xyz.valnet.hadean.gameobjects.ui.tabs.DebugTab;

public class App {

  // The window handle
  private long window;
  private int width = 1024, height = 576;
  private Matrix4f matrix = Matrix4f.orthographic(0, width, height, 0, 0, 100);
  public static int mouseX, mouseY;
  
  public static long audioContext;
  public static long audioDevice;

  @Deprecated
  public static boolean mouseLeft, mouseMiddle, mouseRight;
  
  private Game game;

  public void run() {

    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    alcDestroyContext(audioContext);
    alcCloseDevice(audioDevice);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if ( !glfwInit() )
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
    if ( window == NULL )
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if(action == GLFW_RELEASE) {
        game.keyRelease(key);
      } else if(action == GLFW_PRESS) {
        game.keyPress(key);
      } else {
        game.keyRepeat(key);
      }
    });

    glfwSetCursorPosCallback(window, (long window, double xpos, double ypos) -> {
      mouseX = (int) xpos;
      mouseY = (int) ypos;
    });

    glfwSetScrollCallback(window, (long window, double xOffset, double yOffset) -> {
      if(yOffset > 0)
        game.scrollUp();
      else if(yOffset < 0)
        game.scrollDown();

      // if(yOffset > 0)
      //   game.scrollLeft();
      // else if(yOffset < 0)
      //   game.scrollRight();
    });

    glfwSetMouseButtonCallback(window, (long window, int button, int action, int mods) -> {
        if(action == 1) {
          game.mouseDown(button);
        } else if(action == 0) {
          game.mouseUp(button);
        }

        if(button == GLFW_MOUSE_BUTTON_LEFT) { mouseLeft = action == 1; return; }
        if(button == GLFW_MOUSE_BUTTON_RIGHT) { mouseRight = action == 1; return; }
        if(button == GLFW_MOUSE_BUTTON_MIDDLE) { mouseMiddle = action == 1; return ; }

        DebugTab.log("Mouse: action " + action + " : button " + button);
    });

    // Get the thread stack and push a new frame
    try ( MemoryStack stack = stackPush() ) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      long primaryMonitor = glfwGetPrimaryMonitor();
      GLFWVidMode vidmode = glfwGetVideoMode(primaryMonitor);
      IntBuffer monitorX = stack.mallocInt(1); // int*
      IntBuffer monitorY = stack.mallocInt(1); // int*
      glfwGetMonitorPos(primaryMonitor, monitorX, monitorY);

      // Center the window
      glfwSetWindowPos(
        window,
        monitorX.get(0) + (vidmode.width() - pWidth.get(0)) / 2,
        monitorY.get(0) + (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically


    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Audio device
    String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
    audioDevice = alcOpenDevice(defaultDeviceName);
    int[] attributes = {0};
    audioContext = alcCreateContext(audioDevice, attributes);
    alcMakeContextCurrent(audioContext);

    ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
    ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

    if(!alCapabilities.OpenAL10) {
      System.err.println("Audio not supported?!");
    }

    // Make the window visible
    glfwShowWindow(window);

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();


        
    float clearBrightness = 0.09f;
    glClearColor(clearBrightness, clearBrightness, clearBrightness, 1.0f);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glDepthMask(true);
    glfwSwapInterval(1);


    game.start();
  }

  private void loop() {
    while (!glfwWindowShouldClose(window)) {
      game.updateViewMatrix(matrix);
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

      game.render();
      game.update();

      glfwSwapBuffers(window);
      glfwPollEvents();
    }
  }

  public App(Game game) {
    this.game = game;
  }
}