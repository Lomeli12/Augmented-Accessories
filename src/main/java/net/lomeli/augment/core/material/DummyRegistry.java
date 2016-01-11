package net.lomeli.augment.core.material;

public class DummyRegistry {
    private DummyMaterial[] materials;

    public DummyRegistry(DummyMaterial[] materials) {
        this.materials = materials;
    }

    public DummyMaterial[] getMaterials() {
        return materials;
    }
}
