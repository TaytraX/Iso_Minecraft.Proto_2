
import management.Rendering;
import window.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Main {
    int width = 800;
    int height = 600;

    private Window windo;
    private Rendering rendering;
    float deltaTime;
    private double lastTime;
    private ProcessInput input;

    public void run() {
        // Calcul du deltaTime
        double currentTime = glfwGetTime();
        if (lastTime == 0.0) lastTime = currentTime;

        deltaTime = (float)(currentTime - lastTime);
        lastTime = currentTime;

        windo = new Window("Test", width, height, true);
        windo.create();

        input = new ProcessInput(windo.getHandle());
        rendering = new Rendering(input, deltaTime, width, height);

        while(!windo.shouldClose()) {
            rendering.render();
            windo.update();
        }
        windo.cleanup();
        rendering.cleanup();
    }

    void main(String[] args) {
        Main engine = new Main();
        engine.run();
    }
}