package management;

import loader.shader.ShaderManager;
import renderers.SimpleRender;

public class Rendering {

    RenderContext glContext;
    private SimpleRender render;
    private ShaderManager shaders;

    public Rendering(int windowWidth, int windowHeight) {
        glContext = new RenderContext();
        glContext.init(windowWidth, windowHeight);
        shaders = new ShaderManager();
        shaders.cacheAllShader();
        render = new SimpleRender(shaders);
        init();
    }

    private void init() {
        render.initialize();
    }

    public void render() {
        render.render();
    }

    public void cleanup() {
        render.cleanup();
        shaders.cleanUpAll();
    }
}