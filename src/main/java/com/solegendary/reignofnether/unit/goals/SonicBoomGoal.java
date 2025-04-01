package com.solegendary.reignofnether.unit.goals;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Consumer;

public class SonicBoomGoal extends GenericTargetedSpellGoal {

    public SonicBoomGoal(Mob mob, int channelTicks, int range,
                         Consumer<LivingEntity> onEntityCast, Consumer<Building> onBuildingCast) {
        super(mob, channelTicks, range, onEntityCast, null, onBuildingCast);
        this.bonusChannelingRange = 10;
    }

    @Override
    public void stopCasting() {
        super.stopCasting();
        if (this.mob.level().isClientSide() && !Keybindings.shiftMod.isDown())
            ((Unit) this.mob).getCheckpoints().clear();
    }

    @Override
    public void stop() {
        // hack fix to stop a weird bug where it gets stopped unexpectedly (serverside)
        // happens when needing to move towards the target first
        if (this.ticksCasting <= 5 && isInRange())
            return;
        super.stop();
    }
}
