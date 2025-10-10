package management;

import loader.shader.ShaderManager;
import renderers.SimpleRender;

public class Rendering {

    final RenderContext glContext;
    private final SimpleRender render;
    private final ShaderManager shaders;
    private final Camera camera;
    private final InputProvider input;

    public Rendering(InputProvider input, float deltaTime, int windowWidth, int windowHeight) {
        glContext = new RenderContext();
        glContext.init(windowWidth, windowHeight);
        camera = new CameraLibre(deltaTime, (float) windowWidth/windowHeight);
        this.input = input;
        shaders = new ShaderManager();
        shaders.cacheAllShader();
        render = new SimpleRender(shaders);
        init();
    }

    private void init() {
        camera.init();
        render.initialize();
    }

    public void render() {
        render.render(camera);
        camera.update(input);
    }

    public void cleanup() {
        render.cleanup();
        shaders.cleanUpAll();
    }
}