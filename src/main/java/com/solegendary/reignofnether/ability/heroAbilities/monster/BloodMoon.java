package com.solegendary.reignofnether.ability.heroAbilities.monster;

//Forces day to night under a blood red moon for the entire world temporarily
//Raises the movement and attack speed of all of your units while active (other monster players' units are unaffected)
//Soul Siphon extends the duration

// play a cave sound and announce "A blood moon rises" in global chat

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class BloodMoon extends HeroAbility {

    public BloodMoon(HeroUnit hero) {
        super(hero, 1, UnitAction.BLOOD_MOON, 240, 0, 0, false);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Blood Moon",
            new ResourceLocation("minecraft", "textures/block/redstone_block.png"),
            hotkey,
            () -> false,
            () -> rank > 0,
            () -> true,
            () -> sendUnitCommand(UnitAction.BLOOD_MOON),
            null,
            getTooltipLines(),
            this
        );
    }

    private static final int DURATION = 60;
    private static final int BONUS_SECONDS_PER_SOUL = 3;

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.blood_moon"), true),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip3", BONUS_SECONDS_PER_SOUL))
        );
    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {

    }
}
