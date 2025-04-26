package com.solegendary.reignofnether.ability;

import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.tps.TPSClientEvents;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;

public class Ability {
    public final UnitAction action; // null for worker building production items (handled specially in BuildingClientEvents)
    public float cooldownMax;
    private float cooldown = 0;
    public final float range; // if <= 0, is melee
    public final float radius; // if <= 0, is single target
    public final boolean canTargetEntities;
    public boolean oneClickOneUse; // if true, a group of units/buildings will use their abilities one by one
    public UnitAction autocastEnableAction = null;
    public UnitAction autocastDisableAction = null;

    private boolean autocast = false;
    public void setAutocast(boolean value, Unit unit) { autocast = value; }
    public boolean getAutocast() { return autocast; }

    public Ability(UnitAction action, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        this.action = action;
        this.cooldownMax = cooldownMax;
        this.range = range;
        this.radius = radius;
        this.canTargetEntities = canTargetEntities;
        this.oneClickOneUse = false;
    }

    public Ability(UnitAction action, int cooldownMax, float range, float radius, boolean canTargetEntities, boolean oneClickOneUse) {
        this.action = action;
        this.cooldownMax = cooldownMax;
        this.range = range;
        this.radius = radius;
        this.canTargetEntities = canTargetEntities;
        this.oneClickOneUse = oneClickOneUse;
    }

    protected void toggleAutocast(/*boolean serverSide*/) {
        //TODO
        //if (!level.isClientSide())  // Why? currently its only called by button
        //    return;

        if (autocast && autocastDisableAction != null) {
            sendUnitCommand(autocastDisableAction);
        } else if (!autocast && autocastEnableAction != null) {
            sendUnitCommand(autocastEnableAction);
        }
    }

    public void tickCooldown(Level level) {
        if (this.cooldown > 0) {
            if (level.isClientSide())
                this.cooldown -= (TPSClientEvents.getCappedTPS() / 20D);
            else
                this.cooldown -= 1;
        }
    }

    public boolean isChanneling(Unit unit) { return false; }

    public float getCooldown() { return this.cooldown; }

    public boolean isOffCooldown() { return this.cooldown <= 0; }

    public void setToMaxCooldown(Unit unit) {
        this.cooldown = cooldownMax;
    }

    public void setToMaxCooldown(BuildingPlacement building) {
        this.cooldown = cooldownMax;
    }

    public void setCooldown(float cooldown, Unit unit) {
        if (unit.level().isClientSide() && cooldown > 0) {
            HudClientEvents.setLowestCdHudEntity();
        }
        this.cooldown = Math.min(cooldown, cooldownMax);
    }

    public void use(Level level, Unit unitUsing, LivingEntity targetEntity) { }

    public void use(Level level, BuildingPlacement buildingUsing, LivingEntity targetEntity) { }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) { }

    public void use(Level level, BuildingPlacement buildingUsing, BlockPos targetBp) { }

    public AbilityButton getButton(Keybinding hotkey, BuildingPlacement placement) {
        return null;
    }
    public AbilityButton getButton(Keybinding hotkey, Unit unit) {
        return null;
    }

    public boolean canBypassCooldown() { return false; }

    public boolean shouldResetBehaviours() { return true; }
}
