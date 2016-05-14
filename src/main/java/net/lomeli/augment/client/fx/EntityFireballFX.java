package net.lomeli.augment.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class EntityFireballFX extends EntityFX {
    private int tickCount;
    private int centerX, centery;
    private int circleRadius;
    private int secondsPerRevolution;

    public EntityFireballFX(World world, double xPos, double yPos, double zPos, double xSpeed, double ySpeed, double zSpeed) {
        super(world, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
    }
}
