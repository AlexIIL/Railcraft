/*******************************************************************************
 * Copyright (c) CovertJaguar, 2011-2016
 * http://railcraft.info
 *
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 ******************************************************************************/
package mods.railcraft.common.blocks.tracks.instances;

import mods.railcraft.common.blocks.tracks.EnumTrack;
import mods.railcraft.common.blocks.tracks.speedcontroller.SpeedControllerReinforced;
import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;

public class TrackReinforcedWye extends TrackWye {

    public TrackReinforcedWye() {
        speedController = SpeedControllerReinforced.instance();
    }

    @Override
    public EnumTrack getTrackType() {
        return EnumTrack.REINFORCED_WYE;
    }

    @Override
    public float getExplosionResistance(Explosion explosion, Entity exploder) {
        return TrackReinforced.RESISTANCE;
    }
}
