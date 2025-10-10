package block;

import org.joml.Vector3f;

public enum Material {

    AERA(0.0f, new VoxelsMesh(new Vector3f(1)), 0),
    DIRT(0.5f, new VoxelsMesh(new Vector3f(1)), 1),
    STONE(1.5f, new VoxelsMesh(new Vector3f(1)), 2),
    WOOD(2.0f, new VoxelsMesh(new Vector3f(1)), 3),
    METAL(5.0f, new VoxelsMesh(new Vector3f(1)), 4);

    final float durabilitiy;
    final VoxelsMesh mesh;
    final int ID;

    Material(float durability, VoxelsMesh mesh, int ID) {
        this.durabilitiy = durability;
        this.mesh = mesh;
        this.ID = ID;
    }

    public Vector3f getSize() {
        return mesh.size();
    }
}