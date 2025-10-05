package renderers;

import loader.shader.ShaderManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class SimpleRender {
    private int VAO, VBO, EBO;
    private final ShaderManager shaders;

    private final float[] cubeVertices = {
           -1.0f, -1.0f,
            0.0f,  0.0f,
           -1.0f,  1.0f
    };

    public SimpleRender(@NotNull ShaderManager shaders) {
        this.shaders = shaders;
    }

    public void initialize() {
        setupBuffers();
    }

    private void setupBuffers() {
        // Génération des buffers
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        // Buffer des vertices
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        FloatBuffer vertexBuffer = org.lwjgl.BufferUtils.createFloatBuffer(cubeVertices.length);
        vertexBuffer.put(cubeVertices).flip();

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        // Attribut 0: Position (x, y)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    public void render() {
        shaders.getShaderCache("background").use();

        glBindVertexArray(VAO);
        glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    public void cleanup() {
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);

        shaders.getShaderCache("background").stop();
    }
}