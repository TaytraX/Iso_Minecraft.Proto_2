package management;

import org.joml.Vector2f;

public interface InputProvider {
    boolean isKeyDown(Key key);
    Vector2f getMouseDelta();
}