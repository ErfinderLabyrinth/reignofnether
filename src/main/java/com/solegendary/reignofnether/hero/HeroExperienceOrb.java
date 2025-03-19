package com.solegendary.reignofnether.hero;

import com.google.common.collect.Lists;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

import java.util.List;
import java.util.Random;

public class HeroExperienceOrb extends ExperienceOrb {

    private static final Random RANDOM = new Random();

    private LivingEntity followingHero = null;

    public HeroExperienceOrb(EntityType<? extends ExperienceOrb> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static HeroExperienceOrb newOrb(Level pLevel, HeroUnit heroUnit, double pX, double pY, double pZ, int pValue) {
        HeroExperienceOrb expOrb = new HeroExperienceOrb(EntityRegistrar.HERO_EXPERIENCE_ORB.get(), pLevel);
        expOrb.followingHero = (LivingEntity) heroUnit;
        expOrb.setPos(pX, pY, pZ);
        expOrb.setYRot((float)(RANDOM.nextDouble() * 360.0));
        expOrb.setDeltaMovement(
            (RANDOM.nextDouble() * 0.2 - 0.1) * 2.0,
            RANDOM.nextDouble() * 0.2 * 2.0,
            (RANDOM.nextDouble() * 0.2 - 0.1) * 2.0
        );
        expOrb.value = pValue;
        return expOrb;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.followingHero != null) {
            Vec3 vec3 = new Vec3(
                this.followingHero.getX() - this.getX(),
                this.followingHero.getY() + (double)this.followingHero.getEyeHeight() / 2.0 - this.getY(),
                this.followingHero.getZ() - this.getZ()
            );
            double d0 = vec3.lengthSqr();
            if (d0 < 64.0) {
                double d1 = 1.0 - Math.sqrt(d0) / 8.0;
                this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(d1 * d1 * 0.1)));
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        if (this.onGround) {
            BlockPos pos = new BlockPos(this.getX(), this.getY() - 1.0, this.getZ());
            f = this.level.getBlockState(pos).getFriction(this.level, pos, this) * 0.98F;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply((double)f, 0.98, (double)f));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
        }

        checkTouchedHero();
    }

    private void checkTouchedHero() {
        if (followingHero != null && !followingHero.isDeadOrDying() && !this.level.isClientSide()) {
            AABB aabb = followingHero.getBoundingBox().inflate(1.0, 0.5, 1.0);
            if (aabb.contains(this.position())) {
                followingHero.take(this, count);
                if (count > 0) {
                    ((HeroUnit) followingHero).addExperience(count);
                }
                this.discard();
            }
        }
    }

    @Override
    protected void scanForEntities() {
        super.scanForEntities();
        this.followingPlayer = null;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }
}
