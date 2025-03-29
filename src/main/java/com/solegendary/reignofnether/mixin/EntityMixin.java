package com.solegendary.reignofnether.mixin;

import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.solegendary.reignofnether.resources.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private static double viewScale;
    @Shadow private AABB bb;
    @Shadow public abstract EntityType<?> getType();

    @Inject(
        method = "shouldRenderAtSqrDistance(D)Z",
        at = @At("HEAD"),
        cancellable=true
    )
    private void shouldRenderAtSqrDistance(
            double pDistance, CallbackInfoReturnable<Boolean> cir
    ) {
        if (!OrthoviewClientEvents.isEnabled() || this.getType() != EntityType.ITEM)
            return;

        double d0 = this.bb.getSize();
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }
        // make item entities render at 4x normal distance
        d0 *= 64.0D * viewScale * 4;
        cir.setReturnValue(pDistance < d0 * d0);
    }

    // use this mixin if you want a mob to avoid damage and not even register a damage animation
    @Inject(
            method = "isInvulnerableTo",
            at = @At("HEAD"),
            cancellable=true
    )
    private void isInvulnerableTo(DamageSource pSource, CallbackInfoReturnable<Boolean> cir) {
        if (pSource == damageSources().inWall())
            cir.setReturnValue(true);
    }

    @Shadow public int getTicksRequiredToFreeze() { return 140; }
    @Shadow public int getTicksFrozen() { return 0; }

    @Shadow public abstract DamageSources damageSources();
    @Shadow public abstract Component getName();
    @Shadow public abstract void remove(Entity.RemovalReason pReason);
    @Shadow public abstract AABB getBoundingBox();
    @Shadow public abstract BlockPos getOnPos();
    @Shadow public abstract Level level();

    @Inject(
            method = "getPercentFrozen",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void getPercentFrozen(CallbackInfoReturnable<Float> cir) {
        int i = this.getTicksRequiredToFreeze();
        float percent = (float)Math.min(this.getTicksFrozen(), 140) / (float)i;
        cir.setReturnValue(Math.min(percent, 0.5f));
    }

    @Inject(
            method = "collide",
            at = @At("TAIL"),
            cancellable = true
    )
    public void collide(Vec3 pVec, CallbackInfoReturnable<Vec3> cir) {
        if (!getName().getString().contains("magma"))
            return;

        Vec3 result = cir.getReturnValue();

        boolean isNearLeafOrLog = false;

        AABB aabb = getBoundingBox().inflate(0.5f);
        outerloop:
        for (int x = (int) aabb.minX; x < aabb.maxX; x++) {
            for (int y = (int) aabb.minY; y < aabb.maxY; y++) {
                for (int z = (int) aabb.minZ; z < aabb.maxZ; z++) {
                    BlockState bs = level().getBlockState(new BlockPos(x,y,z));
                    if (BlockUtils.isLogBlock(bs) ||
                        BlockUtils.isFallingLogBlock(bs) ||
                        BlockUtils.isLeafBlock(bs)) {
                        isNearLeafOrLog = true;
                        break outerloop;
                    }
                }
            }
        }
        if (!isNearLeafOrLog)
            return;

        BlockState bs = level().getBlockState(getOnPos());
        BlockState bsBelow = level().getBlockState(getOnPos().below());

        if (BlockUtils.isLogBlock(bs) || BlockUtils.isLogBlock(bsBelow) ||
            BlockUtils.isFallingLogBlock(bs) || BlockUtils.isFallingLogBlock(bsBelow) ||
            BlockUtils.isLeafBlock(bs) || BlockUtils.isLeafBlock(bsBelow)) {
            cir.setReturnValue(new Vec3(pVec.x, pVec.y, pVec.z));
        } else {
            cir.setReturnValue(new Vec3(pVec.x, result.y, pVec.z));
        }
    }
}
