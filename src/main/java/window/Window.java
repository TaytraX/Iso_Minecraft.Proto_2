package window;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Fenêtre pure GLFW sans aucune dépendance OpenGL.
 * Responsabilité : création fenêtre, gestion événements, lifecycle.
 */
public class Window {
    private final String title;
    private int width, height;
    private long windowHandle;
    private final boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    /**
     * Initialise GLFW et crée la fenêtre.
     * N'initialise PAS OpenGL.
     */
    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        configureWindowHints();
        createWindow();
        centerWindow();
        setupCallbacks();

        // Active le contexte GLFW (nécessaire pour OpenGL plus tard)
        glfwMakeContextCurrent(windowHandle);

        if (vSync) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);
    }

    private void configureWindowHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    }

    private void createWindow() {
        windowHandle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
    }

    private void centerWindow() {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidMode != null) {
            glfwSetWindowPos(
                    windowHandle,
                    (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2
            );
        }
    }

    private void setupCallbacks() {
        glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> {
            this.width = w;
            this.height = h;
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void cleanup() {
        Callbacks.glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
    }

    // Getters (pas de logique OpenGL)
    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public long getHandle() {
        return windowHandle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}