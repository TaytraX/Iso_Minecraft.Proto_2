package management;

import block.WorldCoord;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static management.Key.*;

public final class CameraLibre extends Camera {
    float deltaTime;
    float aspectRatio;
    float lastX, lastY;

    private FloatBuffer matrixBufferView;
    private FloatBuffer matrixBufferProjection;

    private WorldCoord cameraPos;
    private Vector3f cameraFront;

    private Matrix4f view;
    private Matrix4f projection;

    private Vector3f cameraUp;
    private Vector3f cameraRight;

    boolean firstMouse = true;

    // Default camera values
    float YAW         = -90.0f;
    float PITCH       =  0.0f;
    float SPEED       =  5.0f;
    float SENSITIVITY =  0.1f;
    float ZoomSENSITIVITY =  2.0f;
    float FOV         =  45.0f;

    enum Camera_Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public CameraLibre(float deltaTime, float aspectRatio) {
        super(deltaTime, aspectRatio);
    }

    public void init() {
        view = new Matrix4f();
        matrixBufferView = BufferUtils.createFloatBuffer(16);
        view.get(matrixBufferView);

        projection = new Matrix4f().perspective((float)Math.toRadians(FOV), aspectRatio, 0.1f, 100.0f);
        matrixBufferProjection = BufferUtils.createFloatBuffer(16);
        projection.get(matrixBufferProjection);

        cameraFront = new Vector3f(0.0f, 0.0f, -2.0f);
        cameraPos = new WorldCoord(0.0f, 0.0f, 10.0f);
        cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
    }

    public void update(InputProvider input) {
        processKeyboard(input);
        mouse_callback(input.getMouseDelta().x, input.getMouseDelta().y);
        updateViewMatrix();
    }

    private void processKeyboard(InputProvider input) {
        if (input.isKeyDown(W)) applyMouvement(Camera_Movement.FORWARD);
        else if (input.isKeyDown(Key.S)) applyMouvement(Camera_Movement.BACKWARD);

        if (input.isKeyDown(A)) applyMouvement(Camera_Movement.LEFT);
        else if (input.isKeyDown(Key.D)) applyMouvement(Camera_Movement.RIGHT);
    }

    private void applyMouvement(Camera_Movement mouvement) {
        float velocity = SPEED * deltaTime;
        Vector3f movement = new Vector3f();

        if (mouvement == Camera_Movement.FORWARD)
            movement.add(new Vector3f(cameraFront).mul(velocity));
        if (mouvement == Camera_Movement.BACKWARD)
            movement.sub(new Vector3f(cameraFront).mul(velocity));
        if (mouvement == Camera_Movement.LEFT)
            movement.sub(new Vector3f(cameraRight).mul(velocity));
        if (mouvement == Camera_Movement.RIGHT)
            movement.add(new Vector3f(cameraRight).mul(velocity));
        if (mouvement == Camera_Movement.UP)
            movement.add(new Vector3f(cameraUp).mul(velocity));
        if (mouvement == Camera_Movement.DOWN)
            movement.sub(new Vector3f(cameraUp).mul(velocity));

        cameraPos.getPosition().add(movement);
    }

    private void mouse_callback(float xpos, float ypos) {
        if (firstMouse)
        {
            lastX = xpos;
            lastY = ypos;
            firstMouse = false;
        }

        float xoffset = xpos - lastX;
        float yoffset = lastY - ypos;
        lastX = xpos;
        lastY = ypos;

        xoffset *= SENSITIVITY;
        yoffset *= SENSITIVITY;

        YAW   += xoffset;
        PITCH += yoffset;

        if(PITCH > 89.0f)
            PITCH = 89.0f;
        if(PITCH < -89.0f)
            PITCH = -89.0f;

        Vector3f direction = new Vector3f();
        direction.x = (float) (cos(Math.toRadians(YAW)) * cos(Math.toRadians(PITCH)));
        direction.y = (float) sin(Math.toRadians(PITCH));
        direction.z = (float) (sin(Math.toRadians(YAW)) * cos(Math.toRadians(PITCH)));
        cameraFront = direction.normalize();
        cameraRight = new Vector3f(cameraFront).cross(cameraUp).normalize();
    }

    public void updateViewMatrix() {
        view.identity();
        view.lookAt(
                cameraPos.getPosition(),
                new Vector3f(cameraPos.getPosition()).add(cameraFront),
                cameraUp
        );
        matrixBufferView.clear();
        view.get(matrixBufferView);
    }

    public Matrix4f getView() {
        return view;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public WorldCoord getPosition() {
        return cameraPos;
    }
}