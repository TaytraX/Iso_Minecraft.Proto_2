package renderers;

import chunk.Chunk;
import loader.shader.Shader;
import loader.shader.ShaderManager;
import management.Camera;
import management.CameraIso;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class SimpleRender {
    private int VAO, VBO, EBO;
    private final Shader shader;
    private final Chunk chunk;

    // Vertices d'un cube 3D (position + coordonnées de texture)
    private final float[][] cubeVertices = {
            {
                    // Face du DESSUS (Y=1)  vue d'en haut
                    0.0f, 1.0f, 0.0f,         0.0f, 1.0f,  // coin arrière-gauche
                    1.0f, 1.0f, 0.0f,         1.0f, 1.0f,  // coin arrière-droite
                    1.0f, 1.0f, 1.0f,         1.0f, 0.0f,  // coin avant-droite
                    0.0f, 1.0f, 1.0f,         0.0f, 0.0f,  // coin avant-gauche
            },
            {
                    // Face DROITE (X=1)      côté droit du cube
                    1.0f, 0.0f, 0.0f,         0.0f, 0.0f,  // arrière bas
                    1.0f, 0.0f, 1.0f,         1.0f, 0.0f,  // avant-bas
                    1.0f, 1.0f, 1.0f,         1.0f, 1.0f,  // avant-haut
                    1.0f, 1.0f, 0.0f,         0.0f, 1.0f,  // arrière-haut
            },
            {
                    // Face ARRIÈRE (Z=0) - face du fond
                    0.0f, 0.0f, 0.0f,         1.0f, 0.0f,  // gauche bas
                    0.0f, 1.0f, 0.0f,         1.0f, 1.0f,  // gauche haut
                    1.0f, 1.0f, 0.0f,         0.0f, 1.0f,  // droite haut
                    1.0f, 0.0f, 0.0f,         0.0f, 0.0f   // droite basse
            }
    };

    // Indices pour les 3 faces visibles en isométrique
    private final int[] indices = {
            // Face du dessus
            0, 2, 1,   0, 3, 2,
            // Face droite
            4, 6, 5,   4, 7, 6,
            // Face arrière
            8, 10, 9,  8, 11, 10
    };

    public SimpleRender(@NotNull ShaderManager shaders) {
        this.shader = shaders.getShaderCache("backgound");
        chunk = new Chunk();
    }

    public void initialize() {
        setupBuffers();
    }

    private void setupBuffers() {
        chunk.init();
        // Génération des buffers
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        // Buffer des vertices
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        FloatBuffer vertexBuffer = org.lwjgl.BufferUtils.createFloatBuffer(120);

        for (float[] face : cubeVertices) {
            vertexBuffer.put(face);
        }
        vertexBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Buffer des indices
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);

        IntBuffer indexBuffer = org.lwjgl.BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // Attribut 0: Position (x, y, z)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    public void render(@NotNull Camera camera) {
        shader.use();
        glBindVertexArray(VAO);

        // Créer et envoyer la matrice de projection isométrique
        shader.uniform().setMatrix4f("u_viewMatrix", camera.getView());
        shader.uniform().setMatrix4f("u_projectionMatrix", camera.getProjection());

        // Parcourir tous les chunks chargés
        chunk.getChunks().forEach((LocalBlockCoord, Block) -> {
            // Matrice de transformation pour positionner le bloc
            Matrix4f modelMatrix = new Matrix4f();
            modelMatrix.translate(LocalBlockCoord.x(), LocalBlockCoord.y(), LocalBlockCoord.z()).scale(Block.getMaterial().getSize());

            shader.uniform().setMatrix4f("u_modelMatrix", modelMatrix);

            // Dessiner le cube
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        });
    }

    public void cleanup() {
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);

        shader.stop();
    }
}