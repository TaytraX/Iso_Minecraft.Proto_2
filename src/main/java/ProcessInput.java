import management.InputProvider;
import management.Key;
import org.joml.Vector2f;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class ProcessInput implements InputProvider {

    private float lastX = 0, lastY = 0;
    private float currentX = 0, currentY = 0;
    private boolean firstMouse = true;
    private final Vector2f mouseDelta = new Vector2f();
    private final long id;

    private static final Map<Key, Integer> KEY_MAPPING = Map.of(
            Key.W, GLFW_KEY_W,
            Key.A, GLFW_KEY_A,
            Key.D, GLFW_KEY_D,
            Key.S, GLFW_KEY_S,
            Key.SPACE, GLFW_KEY_SPACE,
            Key.SHIFT, GLFW_KEY_LEFT_SHIFT
    );

    public ProcessInput(long window) {
        this.id = window;

        // Enregistre le callback UNE SEULE FOIS
        glfwSetCursorPosCallback(window, (win, xPos, yPos) -> {
            currentX = (float) xPos;
            currentY = (float) yPos;

            // Évite un saut au premier mouvement
            if (firstMouse) {
                lastX = currentX;
                lastY = currentY;
                firstMouse = false;
            }
        });
    }

    @Override
    public boolean isKeyDown(Key key) {
        Integer glfwKey = KEY_MAPPING.get(key);
        if (glfwKey == null) return false;  // Sécurité
        return glfwGetKey(id, glfwKey) == GLFW_PRESS;
    }

    @Override
    public Vector2f getMouseDelta() {
        // Calcule le delta
        float deltaX = currentX - lastX;
        float deltaY = currentY - lastY;  // Attention: Y inversé sur certains systèmes

        // Sauvegarde pour la prochaine frame
        lastX = currentX;
        lastY = currentY;

        return mouseDelta.set(deltaX, deltaY);
    }
}