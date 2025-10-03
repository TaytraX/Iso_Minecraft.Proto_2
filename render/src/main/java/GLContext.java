import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class GLContext {

    public void init(long windowID) {
        createCapabilities();
        glViewport(windowID, );
    }
}