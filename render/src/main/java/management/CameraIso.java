package management;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public final class CameraIso extends Camera {
    float deltaTime;

    float aspectRatio;
    float lastX, lastY;

    private Matrix4f view;
    private FloatBuffer matrixBufferView;

    private Matrix4f projection;
    private FloatBuffer matrixBufferProjection;

    private Vector3f cameraUp;
    private Vector3f cameraRight;

    // Default camera values
    float YAW         = -90.0f;
    float PITCH       =  0.0f;
    float SPEED       =  2.5f;
    float SENSITIVITY =  0.1f;
    float ZoomSENSITIVITY =  2.0f;
    float FOV         =  45.0f;

    private Vector3f cameraPos;
    private Vector3f cameraFront;

    enum Camera_Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public CameraIso(float deltaTime, float aspectRatio) {
        super(deltaTime, aspectRatio);
    }

    public void init() {
        view = new Matrix4f();
        matrixBufferView = BufferUtils.createFloatBuffer(16);
        view.get(matrixBufferView);

        projection = createIsometricProjectionMatrix();
        matrixBufferProjection = BufferUtils.createFloatBuffer(16);
        projection.get(matrixBufferProjection);

        cameraFront = new Vector3f(0.0f, 0.0f, -2.0f);
        cameraPos = new Vector3f(0, 5, 0);
        cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
    }

    @Override
    public void update(InputProvider input) {
        updateViewMatrix();
    }

    private void applyMouvement(Camera_Movement mouvement) {
        float velocity = SPEED * deltaTime;

        switch (mouvement) {
            case FORWARD -> cameraPos.z += velocity;
            case BACKWARD -> cameraPos.z -= velocity;
            case LEFT -> cameraPos.x -= velocity;
            case RIGHT -> cameraPos.x += velocity;
            case UP -> cameraPos.y += velocity;
            case DOWN -> cameraPos.y -= velocity;
        }
    }

    /**
     * Crée la matrice de projection isométrique
     */
    private Matrix4f createIsometricProjectionMatrix() {
        Matrix4f matrix = new Matrix4f();

        // Projection orthogonale
        matrix.ortho(-10.0f, 10.0f, -10.0f, 10.0f, -100.0f, 100.0f);


        return matrix;
    }

    public void updateViewMatrix() {
        view.identity();

        // D'abord, on applique les rotations isométriques
        view.rotateX((float) Math.toRadians(-35.264f));
        view.rotateY((float) Math.toRadians(45));

        // Optionnel : échelle pour ajuster la taille
        view.scale(0.75f, 1.15f, 0.75f);

        view.translate(new Vector3f(cameraPos).negate());

        matrixBufferView.clear();
        view.get(matrixBufferView);
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return view;
    }
}