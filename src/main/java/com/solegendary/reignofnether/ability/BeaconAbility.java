package com.solegendary.reignofnether.ability;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.buildings.neutral.Beacon;
import com.solegendary.reignofnether.unit.UnitAction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class BeaconAbility extends Ability {

    protected final Beacon beacon;
    protected final MobEffect effect;

    public static final int CD_MAX = 10;

    public BeaconAbility(UnitAction action, MobEffect effect, Beacon beacon) {
        super(
                action,
                beacon.getLevel(),
                CD_MAX,
                0,
                0,
                false,
                true
        );
        this.beacon = beacon;
        this.effect = effect;
    }

    @Override
    public void use(Level level, Building buildingUsing, LivingEntity entity) {
        if (!level.isClientSide()) {
            beacon.setAuraEffect(effect);
            setToMaxCooldown();
        } else {
            setToMaxCooldown();
        }
    }
}
