package loader.shader;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static loader.shader.ShaderSourceLoader.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class ShaderManager {
    private final Map<String, Shader> shaderCache = new HashMap<>();

    public void cacheAllShader() {
        cacheShader("background");
        cacheShader("full");
    }

    private void cacheShader(String shaderName) {
        if(shaderCache.containsKey(shaderName)) return;

        String vertexSource;
        String fragmentSource;
        try {
            fragmentSource = tryLoadFragmentSource(shaderName);
            vertexSource = ShaderSourceLoader.tryLoadVertexSource(shaderName);
        }  catch (NullPointerException e) {
            System.err.println("fichier '" + shaderName + "' introuvable. " + e.getMessage());
            System.out.println("Chargement du shaderSourceLoader par défaut...");
            vertexSource = loadDefaultVertex();
            fragmentSource = loadDefaultFragment();
        }  catch (RuntimeException e) {
            System.err.println("Erreur OpenGL fichier '" + shaderName + "': " + e.getMessage());
            System.out.println("Chargement du shaderSourceLoader par défaut...");
            vertexSource = loadDefaultVertex();
            fragmentSource = loadDefaultFragment();
        } catch (Exception e) { // Fallback pour le reste
            System.err.println("Erreur inattendue fichier '" + shaderName + "': " + e.getMessage());
            vertexSource = loadDefaultVertex();
            fragmentSource = loadDefaultFragment();
        }

        Shader shader = compile(vertexSource, fragmentSource);

        shaderCache.put(shaderName, shader);
    }

    @NotNull
    @Contract("_, _ -> new")
    private Shader compile(String vertexSource, String fragmentSource) {
        // 1. Compile vertex
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            String log = glGetShaderInfoLog(vertexID);
            glDeleteShader(vertexID);
            throw new ShaderCompilationException("Vertex", log);
        }

        // 2. Compile fragment
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            String log = glGetShaderInfoLog(fragmentID);
            glDeleteShader(vertexID);
            glDeleteShader(fragmentID);
            throw new ShaderCompilationException("Fragment", log);
        }

        // 3. Link program
        int programID = glCreateProgram();
        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);
        glLinkProgram(programID);

        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            String log = glGetProgramInfoLog(programID);
            glDeleteShader(vertexID);
            glDeleteShader(fragmentID);
            glDeleteProgram(programID);
            throw new RuntimeException("Linking failed: " + log);
        }

        glDetachShader(programID, vertexID);
        glDetachShader(programID, fragmentID);
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);

        // 5. Parse uniforms
        UniformManager uniforms = new UniformManager(programID);
        uniforms.parseUniformsFromSource(vertexSource + '\n' + fragmentSource);

        return new Shader(programID, uniforms);
    }

    public Shader getShaderCache(String name) {
        return shaderCache.get(name);
    }

    public void cleanUpAll() {
        shaderCache.values().forEach(Shader::cleanUp);
        shaderCache.clear();
    }
}