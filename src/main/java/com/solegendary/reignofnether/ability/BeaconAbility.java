package com.solegendary.reignofnether.ability;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.buildings.neutral.Beacon;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.packets.BeaconSyncClientboundPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;

import static com.solegendary.reignofnether.resources.ResourceCost.TICKS_PER_SECOND;

public abstract class BeaconAbility extends Ability {

    protected final Beacon beacon;
    protected final MobEffect effect;

    public static final int CD_MAX = 5 * TICKS_PER_SECOND;

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

    private void setToMaxCooldownAllAbiltities() {
        for (Ability ability : beacon.getAbilities())
            ability.setToMaxCooldown();
    }

    @Override
    public void use(Level level, Building buildingUsing, BlockPos bp) {
        beacon.setAuraEffect(effect);
        setToMaxCooldownAllAbiltities();
    }
}
