package management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;

class RenderContextTest {

    int width = 800;
    int height = 600;

    @Test
    void init() {
        RenderContext context = new RenderContext();
        context.init(width, height);

        assertNotEquals(0, GL_VERSION);
    }
}