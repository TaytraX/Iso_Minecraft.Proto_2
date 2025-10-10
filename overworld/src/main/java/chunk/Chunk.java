package chunk;

import block.LocalCoord;
import block.Material;
import block.Placeable;
import block.SimpleBlock;

import java.util.concurrent.ConcurrentHashMap;

public class Chunk {
    private final ConcurrentHashMap<LocalCoord, Placeable> chunks = new ConcurrentHashMap<>();

    public void init() {
        addBlock(0, 0, 0, Material.STONE);
        addBlock(0, 1, 0, Material.DIRT);
    }

    private void addBlock(int x, int y, int z, Material material) {
        chunks.put(new LocalCoord(x, y, z), new SimpleBlock(new LocalCoord(x, y, z), material));
    }

    public Placeable getBlock(LocalCoord localCoord) {
        return chunks.get(localCoord);
    }

    public ConcurrentHashMap<LocalCoord, Placeable> getChunks() {
        return chunks;
    }
}