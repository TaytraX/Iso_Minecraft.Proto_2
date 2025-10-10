package management;

import block.WorldCoord;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract sealed class Camera permits CameraLibre, CameraIso {

    protected float deltaTime;
    protected float aspectRatio;

    protected Matrix4f view;
    protected Matrix4f projection;

    public WorldCoord position;

    protected Camera(float deltaTime, float aspectRatio) {
        this.deltaTime = deltaTime;
        this.aspectRatio = aspectRatio;
    }

    public abstract void init();

    public abstract void update(InputProvider input);

    public Matrix4f getView() {
        return view;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public WorldCoord getPosition() {
        return position;
    }
}