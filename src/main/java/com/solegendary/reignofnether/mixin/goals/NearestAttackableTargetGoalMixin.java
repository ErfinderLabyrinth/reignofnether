package com.solegendary.reignofnether.mixin.goals;

import com.solegendary.reignofnether.unit.NonUnitServerEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {

    public boolean canUse() { return false; }
    public NearestAttackableTargetGoalMixin(Mob pMob, boolean pMustSee) { super(pMob, pMustSee); }

    @Inject(
            method = "findTarget",
            at = @At("HEAD"),
            cancellable = true
    )
    public void findTarget(CallbackInfo ci) {
        synchronized (NonUnitServerEvents.controlledNonUnits) {
            if (mob instanceof PathfinderMob pfMob && NonUnitServerEvents.controlledNonUnits.contains(pfMob))
                ci.cancel();
        }
    }
}
