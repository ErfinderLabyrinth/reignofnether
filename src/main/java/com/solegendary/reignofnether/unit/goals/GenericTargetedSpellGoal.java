package com.solegendary.reignofnether.unit.goals;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.ability.AbilityClientboundPacket;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.unit.UnitAnimationAction;
import com.solegendary.reignofnether.unit.packets.UnitAnimationClientboundPacket;
import com.solegendary.reignofnether.util.MyMath;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class GenericTargetedSpellGoal extends MoveToTargetBlockGoal {

    protected LivingEntity targetEntity = null;
    protected Ability ability; // used for syncing cooldown with clientside
    protected int ticksCasting = 0; // how long have we spent trying to cast this spell
    public boolean isCasting() { return isCasting; }
    protected final int channelTicks; // max time required to cast a spell
    protected boolean isCasting = false;
    protected BlockPos castTarget = null; // pos that the spell will be cast at
    protected final int range;
    public Consumer<LivingEntity> onEntityCast;
    public Consumer<BlockPos> onGroundCast;
    public Consumer<BuildingPlacement> onBuildingCast;
    public boolean hasKeyframeAnimations = false;
    protected int bonusChannelingRange = 0; // extra range added while channeling
    protected final UnitAnimationAction animationStart;

    public GenericTargetedSpellGoal(Mob mob, int channelTicks, int range,
                                    UnitAnimationAction animationStart,
                                    @Nullable Consumer<LivingEntity> onEntityCast,
                                    @Nullable Consumer<BlockPos> onGroundCast,
                                    @Nullable Consumer<BuildingPlacement> onBuildingCast) {
        super(mob, false, 0);
        this.channelTicks = channelTicks;
        this.range = range;
        this.animationStart = animationStart;
        this.onEntityCast = onEntityCast;
        this.onGroundCast = onGroundCast;
        this.onBuildingCast = onBuildingCast;
    }

    public GenericTargetedSpellGoal(Mob mob, int channelTicks, int range,
                                    @Nullable Consumer<LivingEntity> onEntityCast,
                                    @Nullable Consumer<BlockPos> onGroundCast,
                                    @Nullable Consumer<BuildingPlacement> onBuildingCast) {
        super(mob, false, 0);
        this.channelTicks = channelTicks;
        this.range = range;
        this.animationStart = null;
        this.onEntityCast = onEntityCast;
        this.onGroundCast = onGroundCast;
        this.onBuildingCast = onBuildingCast;
    }

    // if we set an entity target, on every tick we will follow that target
    // if we set a BlockPos as the target, remove any entity target
    // the user will only start casting once we reach the target
    public void setTarget(LivingEntity entity) {
        this.targetEntity = entity;
    }
    public void setTarget(BlockPos bpTarget) {
        this.setMoveTarget(bpTarget);
        this.setTarget((LivingEntity) null);
    }
    public void setTarget(BuildingPlacement building) {
        this.setMoveTarget(building.centrePos);
        this.setTarget((LivingEntity) null);
    }

    public LivingEntity getTargetEntity() {
        return targetEntity;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    protected boolean isInRange() {
        int finalRange = range;
        if (isCasting())
            finalRange += bonusChannelingRange;

        if (moveTarget != null && MyMath.distance(
                this.mob.getX(), this.mob.getZ(),
                moveTarget.getX(), moveTarget.getZ()) <= finalRange)
            return true;
        if (castTarget != null && MyMath.distance(
                this.mob.getX(), this.mob.getZ(),
                castTarget.getX(), castTarget.getZ()) <= finalRange)
            return true;
        return false;
    }

    @Override
    public void tick() {
        // keep following the target
        if (this.targetEntity != null)
            this.setMoveTarget(this.targetEntity.getOnPos());

        if (moveTarget != null || castTarget != null) {

            if (isInRange()) {
                if (moveTarget != null)
                    castTarget = moveTarget;
                if (!isCasting)
                    startCasting();
                this.stopMoving();
            } else {
                this.stopCasting();
                this.setMoveTarget(moveTarget);
            }

            if (isCasting && castTarget != null) {
                this.mob.getLookControl().setLookAt(castTarget.getX(), castTarget.getY(), castTarget.getZ());
                ticksCasting += 1;
                if (ticksCasting >= channelTicks) {
                    if (onEntityCast != null && targetEntity != null)
                        onEntityCast.accept(targetEntity);
                    else if (onGroundCast != null || onBuildingCast != null) {
                        BuildingPlacement targetBuilding = BuildingUtils.findBuilding(mob.level().isClientSide(), castTarget);
                        if (onBuildingCast != null && targetBuilding != null) {
                            onBuildingCast.accept(targetBuilding);
                        }
                        else if (onGroundCast != null)
                            onGroundCast.accept(castTarget);
                    }
                    if (this.ability != null && !this.mob.level().isClientSide())
                        AbilityClientboundPacket.sendSetCooldownPacket(this.mob.getId(), this.ability.action, this.ability.cooldownMax);
                    this.stopExceptAnimations();
                }
            }
        }
    }

    public void startCasting() {
        this.isCasting = true;
        this.castTarget = moveTarget;
        if (!this.mob.level().isClientSide()) {
            if (!hasKeyframeAnimations) {
                UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.NON_KEYFRAME_START, this.mob);
            } else if (animationStart != null) {
                UnitAnimationClientboundPacket.sendBasicPacket(animationStart, this.mob);
            }
        }
    }
    public void stopCasting() {
        this.isCasting = false;
        this.ticksCasting = 0;
        this.castTarget = null;
        if (!this.mob.level().isClientSide() && ticksCasting < channelTicks) {
            if (!hasKeyframeAnimations) {
                UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.NON_KEYFRAME_STOP, this.mob);
            } else {
                UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.STOP, this.mob);
            }
        }
    }

    @Override
    public void stop() {
        this.stopMoving();
        this.setTarget((LivingEntity) null);
        this.stopCasting();
    }

    public void stopExceptAnimations() {
        this.stopMoving();
        this.setTarget((LivingEntity) null);
        this.isCasting = false;
        this.ticksCasting = 0;
        this.castTarget = null;
    }
}
