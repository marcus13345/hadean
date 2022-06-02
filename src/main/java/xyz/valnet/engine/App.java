package xyz.valnet.engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import xyz.valnet.engine.math.Matrix4f;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class App {

  // The window handle
  private long window;
  private int width = 1024, height = 576;
  private Matrix4f matrix = Matrix4f.orthographic(0, width, height, 0, 0, 100);
  public static int mouseX, mouseY;
  
  @Deprecated
  public static boolean mouseLeft, mouseMiddle, mouseRight;
  
  private Game game;

  public void run() {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

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
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
    if ( window == NULL )
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
    });
    

    glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
      @Override
      public void invoke(long window, double xpos, double ypos) {
        mouseX = (int) xpos;
        mouseY = (int) ypos;
      }
    });

    glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
      @Override
      public void invoke(long window, int button, int action, int mods) {
        if(action == 1) {
          game.mouseDown(button);
        } else if(action == 0) {
          game.mouseUp(button);
        }

        // TODO deprecate these.
        if(button >= 3) return;
        if(button == GLFW_MOUSE_BUTTON_LEFT) { mouseLeft = action == 1; return; }
        if(button == GLFW_MOUSE_BUTTON_RIGHT) { mouseRight = action == 1; return; }
        if(button == GLFW_MOUSE_BUTTON_MIDDLE) { mouseMiddle = action == 1; return ; }
        
      }
    });

    // Get the thread stack and push a new frame
    try ( MemoryStack stack = stackPush() ) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
        window,
        (vidmode.width() - pWidth.get(0)) / 2,
        (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

        
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
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