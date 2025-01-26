package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ability.BeaconAbility;
import com.solegendary.reignofnether.building.buildings.neutral.Beacon;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class BeaconRegeneration extends BeaconAbility {

    public final static MobEffect AURA_EFFECT = MobEffects.REGENERATION;

    public BeaconRegeneration(Beacon beacon) {
        super(UnitAction.BEACON_REGENERATION, AURA_EFFECT, beacon);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton(
                "Regeneration Aura",
                new ResourceLocation("minecraft", "textures/mob_effect/regeneration.png"),
                hotkey,
                () -> beacon.getAuraEffect() == AURA_EFFECT,
                () -> false,
                () -> beacon.getUpgradeLevel() >= 1,
                () -> UnitClientEvents.sendUnitCommand(UnitAction.BEACON_REGENERATION),
                null,
                List.of(
                        FormattedCharSequence.forward(I18n.get("ability.reignofnether.beacon_aura.regeneration"), Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("ability.reignofnether.beacon_aura.regeneration.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("ability.reignofnether.beacon_aura.one_aura"), Style.EMPTY)
                ),
                this
        );
    }
}