package loader.texture;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

class TextureLoader {

    @NotNull
    public static byte[] tryLoadEmbeddedTexture(String filename) {
        final String[] extensions = {".png", ".jpg", ".jpeg"};
        InputStream inputStream = null;

        try {
            for (int i = 0; i < extensions.length; i++) {
                inputStream = TextureLoader.class.getResourceAsStream("/textures/" + filename + extensions[i]);

                if (inputStream != null) break;
                else if (i == 2) throw new IOException("file not found");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert inputStream != null;
        try {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}