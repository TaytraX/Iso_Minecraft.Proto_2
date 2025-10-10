package block;

public sealed abstract class Placeable permits OrientedBlock , SimpleBlock {
    protected final LocalCoord localCoord;
    protected final Material material;

    protected Placeable(LocalCoord localCoord, Material material) {
        this.localCoord = localCoord;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}