package loader.shader;

public class ShaderCompilationException extends RuntimeException {
    public ShaderCompilationException(String message, String log) {
        super(message);
    }
}
