package net.lomeli.augment.client.lib;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.augment.client.fx.EntityFireballFX;

public enum EnumModParticles {
    FIRE_BALL(0, EntityFireballFX.class);
    private final int id;
    private final Class<?> fxClass;

    EnumModParticles(int id, Class<?> fxClass) {
        this.id = id;
        this.fxClass = fxClass;
    }

    public int getId() {
        return id;
    }

    public Class<?> getFxClass() {
        return fxClass;
    }

    @SideOnly(Side.CLIENT)
    public EntityFX getParticle(World world, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        try {
            return (EntityFX) fxClass.getConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class)
                    .newInstance(world, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
