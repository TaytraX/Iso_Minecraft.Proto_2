package window;

import management.RenderContext;
import org.junit.jupiter.api.Test;

class WindowTest {
    Window window;

    @Test
    void create() {
        window = new Window("Test", 800, 600, true);
        window.create();
        RenderContext context = new RenderContext();
        context.init(800, 600);
    }

    @Test
    void update() {
        create();
        while(window.shouldClose()){
            window.update();
        }
    }
}