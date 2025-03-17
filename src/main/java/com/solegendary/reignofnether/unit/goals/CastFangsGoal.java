package com.solegendary.reignofnether.unit.goals;

import com.solegendary.reignofnether.unit.UnitAnimationAction;
import com.solegendary.reignofnether.unit.packets.UnitAnimationClientboundPacket;
import com.solegendary.reignofnether.unit.packets.UnitSyncClientboundPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Consumer;

public class CastFangsGoal extends AbstractCastTargetedSpellGoal {

    public CastFangsGoal(Mob mob, int channelTicks, int range, Consumer<LivingEntity> onCast) {
        super(mob, channelTicks, range, onCast, null, null);
    }

    @Override
    public void startCasting() {
        super.startCasting();
        if (!this.mob.level().isClientSide())
            UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.NON_KEYFRAME_START, this.mob);
    }

    @Override
    public void stopCasting() {
        super.stopCasting();
        if (!this.mob.level().isClientSide())
            UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.NON_KEYFRAME_STOP, this.mob);
    }
}
