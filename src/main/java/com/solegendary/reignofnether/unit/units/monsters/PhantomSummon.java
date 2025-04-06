package com.solegendary.reignofnether.unit.units.monsters;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

// still not a unit, but has overrides to make

// TODO:
// - Must be able to set a specific entitity to attack
//      - Once that unit dies, the phantom disappears
// - Better control range and height of flight
// - Increase size of the phantoms according to spell level, which also scales damage

public class PhantomSummon extends Phantom {

    final static public float attackDamage = 5.0f;
    final static public float maxHealth = 30.0f;

    public PhantomSummon(EntityType<? extends Phantom> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean removeWhenFarAway(double d) { return false; }



    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, PhantomSummon.attackDamage)
                .add(Attributes.MAX_HEALTH, PhantomSummon.maxHealth);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        return pSpawnData;
    }
}
