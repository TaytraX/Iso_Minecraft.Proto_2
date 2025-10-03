package window;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowTest {
    Window window;

    @Test
    void create() {
        window = new Window("Test", 800, 600, true);
        window.create();
    }

    @Test
    void update() {
        create();
        while(!window.shouldClose()){
            window.update();
        }
    }
}