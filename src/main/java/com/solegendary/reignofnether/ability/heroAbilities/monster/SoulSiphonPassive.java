package com.solegendary.reignofnether.ability.heroAbilities.monster;

//The necromancer begins to collect the souls of nearby units that die
//Whenever another spell is cast, all souls up to a maximum are consumed to empower that spell
//Higher levels increase the maximum number of souls held
//can be toggled on and off

// starts at 4/20 souls, raises to 7/30, 10/40 at higher ranks

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.HeroAbility;
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

public class SoulSiphonPassive extends HeroAbility {

    public boolean active = true;
    public int souls = 0;
    public int soulsUsed = 4;
    public int soulsMax = 20;

    public SoulSiphonPassive(HeroUnit hero) {
        super(hero, 1, UnitAction.TOGGLE_SOUL_SIPHON_PASSIVE, 0, 0, 0, false);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Soul Siphon",
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/portal.png"),
            hotkey,
            () -> active,
            () -> rank > 0,
            () -> true,
            () -> sendUnitCommand(UnitAction.TOGGLE_SOUL_SIPHON_PASSIVE),
            null,
            getTooltipLines(),
            this
        );
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.soul_siphon"), true),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip3", soulsUsed, soulsMax)),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip4"))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank1"), rank == 0),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank2"), rank == 1),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank3"), rank == 2)
        );
    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        active = !active;
    }
}
