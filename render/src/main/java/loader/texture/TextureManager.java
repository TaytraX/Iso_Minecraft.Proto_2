package loader.texture;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public final class TextureManager {
    private final Map<String, Texture> textureCache = new HashMap<>();

    public void cacheAll() {
        createAndCacheDefaultTexture();
        cacheTexture("player");
        cacheTexture("awesomeface");
    }

    private void cacheTexture(String name) {
        if (textureCache.containsKey(name)) return;

        byte[] data = TextureLoader.tryLoadEmbeddedTexture(name);
        Texture texture;
        try {
            texture = loadTexture(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        textureCache.put(name, texture);
    }

    @NotNull
    @Contract("_ -> new")
    private static Texture loadTexture(@NotNull byte[] imageData) throws Exception {
        ByteBuffer imageBuffer;
        ByteBuffer textureBuffer;
        int textureID, width, height;

        // Allouer dans le heap natif (pas la stack !)
        imageBuffer = MemoryUtil.memAlloc(imageData.length);
        imageBuffer.put(imageData);
        imageBuffer.flip();

        // Allouer les IntBuffer sur la stack (ça c’est ok)
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Charger avec STBImage
            textureBuffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4);
            if (textureBuffer == null) {
                MemoryUtil.memFree(imageBuffer);
                throw new Exception("Could not load from memory: " + STBImage.stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        // Libérer l’image brute (on n’en a plus besoin)
        MemoryUtil.memFree(imageBuffer);

        // Génération texture OpenGL
        textureID = GL11.glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureBuffer);

        // Filtrage
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Libérer textureBuffer de stb_image
        STBImage.stbi_image_free(textureBuffer);

        return new Texture(textureID);
    }

    public void createAndCacheDefaultTexture() {
        // TextureLoader 2x2 pixels blancs
        ByteBuffer data = org.lwjgl.BufferUtils.createByteBuffer(16);
        for (int i = 0; i < 16; i++) {
            data.put((byte) 255); // Blanc opaque
        }
        data.flip();

        int textureID = GL11.glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, 2, 2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);

        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        textureCache.put("default", new Texture(textureID));
    }

    public Texture getTexture(String name) {
        if (!textureCache.containsKey(name)) return textureCache.get("default");
        else return textureCache.get(name);
    }

    public void cleanUpAll() {
        textureCache.values().forEach(Texture::cleanUp);
        textureCache.clear();
    }
}