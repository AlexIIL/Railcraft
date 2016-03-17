/******************************************************************************
 * Copyright (c) CovertJaguar, 2011-2016                                      *
 * http://railcraft.info                                                      *
 * *
 * This code is the property of CovertJaguar                                  *
 * and may only be used with explicit written                                 *
 * permission unless otherwise specified on the                               *
 * license page at http://railcraft.info/wiki/info:license.                   *
 ******************************************************************************/

package mods.railcraft.common.util.misc;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

/**
 * Created by CovertJaguar on 3/9/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class AABBFactory {

    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    private AABBFactory() {
        minX = 0;
        minY = 0;
        minZ = 0;
        maxX = 0;
        maxY = 0;
        maxZ = 0;
    }

    private AABBFactory(double x1, double y1, double z1, double x2, double y2, double z2) {
        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        maxZ = Math.max(z1, z2);
    }

    public static AABBFactory make() {
        return new AABBFactory();
    }

    public static AABBFactory make(AxisAlignedBB box) {
        return new AABBFactory(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public AxisAlignedBB build() {
        return AxisAlignedBB.fromBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public AABBFactory fromBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
        minX = Math.min(x1, x2);
        minY = Math.min(y1, y2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = Math.max(y1, y2);
        maxZ = Math.max(z1, z2);
        return this;
    }

    public AABBFactory createBoxForTileAt(BlockPos pos) {
        fromBounds(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                pos.getX() + 1,
                pos.getY() + 1,
                pos.getZ() + 1);
        return this;
    }

    public AABBFactory createBoxForTileAt(BlockPos pos, double grow) {
        fromBounds(
                pos.getX() - grow,
                pos.getY() - grow,
                pos.getZ() - grow,
                pos.getX() + 1.0 + grow,
                pos.getY() + 1.0 + grow,
                pos.getZ() + 1.0 + grow);
        return this;
    }

    public AABBFactory expandHorizontally(double grow) {
        minX -= grow;
        minZ -= grow;
        maxX += grow;
        maxZ += grow;
        return this;
    }

    public AABBFactory raiseFloor(double raise) {
        minY += raise;
        return this;
    }

    public AABBFactory raiseCeiling(double raise) {
        maxY += raise;
        return this;
    }
}
