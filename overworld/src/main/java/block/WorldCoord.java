package block;

import org.joml.Vector3f;

public class WorldCoord {
    private Vector3f position;

    public WorldCoord(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
    }

    public Vector3f getPosition() {
        return position;
    }
}