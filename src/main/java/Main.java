import loader.shader.ShaderManager;
import management.Rendering;
import window.Window;

public class Main {
    int width = 800;
    int height = 600;

    private Window windo;
    private Rendering rendering;

    public void run() {
        ShaderManager shader = new ShaderManager();
        windo = new Window("Test", width, height, true);
        windo.create();


        rendering = new Rendering(width, height);

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