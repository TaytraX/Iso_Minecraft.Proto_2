package management;

import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.glViewport;

public class RenderContext {

    public void init(int width, int height) {
        GL.createCapabilities();
        glViewport(0, 0, width, height);
    }

    public void updateViewport(int width, int height) {
        glViewport(0, 0, width, height);
    }
}