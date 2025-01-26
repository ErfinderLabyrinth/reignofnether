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

public class BeaconResistance extends BeaconAbility {

    public final static MobEffect AURA_EFFECT = MobEffects.DAMAGE_RESISTANCE;

    public BeaconResistance(Beacon beacon) {
        super(UnitAction.BEACON_RESISTANCE, AURA_EFFECT, beacon);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton(
                "Resistance Aura",
                new ResourceLocation("minecraft", "textures/mob_effect/resistance.png"),
                hotkey,
                () -> beacon.getAuraEffect() == AURA_EFFECT,
                () -> false,
                () -> beacon.getUpgradeLevel() >= 1,
                () -> UnitClientEvents.sendUnitCommand(UnitAction.BEACON_RESISTANCE),
                null,
                List.of(
                        FormattedCharSequence.forward(I18n.get("ability.reignofnether.beacon_aura.resistance"), Style.EMPTY),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("ability.reignofnether.beacon_aura.resistance.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("ability.reignofnether.beacon_aura.one_aura"), Style.EMPTY)
                ),
                this
        );
    }
}
