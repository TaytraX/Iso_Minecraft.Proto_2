package loader.shader;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class ShaderSourceLoader {
    @NotNull
    @Contract("_ -> new")
    public static String tryLoadFragmentSource(String shaderName) throws IOException {
        try (InputStream fragmentInputStream = ShaderSourceLoader.class.getResourceAsStream("/shaders/fragment/" + shaderName + ".frag")) {
            if (fragmentInputStream == null) {
                throw new IOException("Fragment shaderSourceLoader file not found: " + shaderName + ".frag");
            }
            return new String(fragmentInputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static String tryLoadVertexSource(String shaderName) throws IOException {
        // Try-with-resources pour auto-close
        try (InputStream vertexInputStream = ShaderSourceLoader.class.getResourceAsStream("/shaders/vertex/" + shaderName + ".vert")) {
            if (vertexInputStream == null) {
                throw new IOException("Vertex shaderSourceLoader file not found: " + shaderName + "..vert");
            }
            return new String(vertexInputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @NotNull
    @Contract(pure = true)
    public static String loadDefaultFragment() {
        return """
                #version 330 core
                out vec4 fragColor;
                void main() { fragColor = vec4(1.0, 1.0, 1.0, 1.0); }""";
    }

    @NotNull
    @Contract(pure = true)
    public static String loadDefaultVertex() {
        return """
                #version 330 core
                in vec3 position;
                void main() { gl_Position = vec4(position, 1.0); }""";
    }
}