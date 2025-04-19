package com.solegendary.reignofnether.mixin.goals;

import com.solegendary.reignofnether.unit.NonUnitServerEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {

    public boolean canUse() { return false; }
    public NearestAttackableTargetGoalMixin(Mob pMob, boolean pMustSee) { super(pMob, pMustSee); }

    @Inject(
            method = "setTarget",
            at = @At("HEAD"),
            cancellable = true
    )
    public void setTarget(LivingEntity pTarget, CallbackInfo ci) {
        synchronized (NonUnitServerEvents.attackSuppressedNonUnits) {
            if (pTarget != null && mob instanceof PathfinderMob pfMob && NonUnitServerEvents.attackSuppressedNonUnits.contains(pfMob)) {
                ci.cancel();
            }
        }
    }
}
