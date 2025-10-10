package loader.texture;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.glActiveTexture;

record Texture(int textureID) {

    public void bind(int order) {
        glActiveTexture(order);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void cleanUp() {
        GL11.glDeleteTextures(textureID);
    }
}