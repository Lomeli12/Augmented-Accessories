package net.lomeli.augment.core.material;

public class DummyMaterial {
    private String material;
    private int level, color;
    private boolean gem;

    public DummyMaterial(String material, int level, int color, boolean gem) {
        this.material = material;
        this.level = level;
        this.color = color;
        this.gem = gem;
    }

    public String getMaterial() {
        return material;
    }

    public int getLevel() {
        return level;
    }

    public int getColor() {
        return color;
    }

    public boolean isGem() {
        return gem;
    }
}
