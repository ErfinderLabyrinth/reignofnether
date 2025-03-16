package com.solegendary.reignofnether.ability.heroAbilities.monster;

//The necromancer begins to collect the souls of nearby units that die
//Whenever another spell is cast, all souls up to a maximum are consumed to empower that spell
//Higher levels increase the maximum number of souls held
//can be toggled on and off

// starts at 4/20 souls, raises to 7/30, 10/40 at higher ranks

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class SoulSiphonPassive extends HeroAbility {

    public boolean active = false;
    public int souls = 0;
    public int soulsPerCast = 0;
    public int soulsMax = 0;

    public SoulSiphonPassive(HeroUnit hero) {
        super(hero, 3, UnitAction.TOGGLE_SOUL_SIPHON_PASSIVE, 0, 0, 0, false);
    }

    public boolean rankUp() {
        if (super.rankUp()) {
            if (rank == 1) {
                soulsPerCast = 4;
                soulsMax = 20;
            } else if (rank == 2) {
                soulsPerCast = 7;
                soulsMax = 30;
            } else if (rank == 3) {
                soulsPerCast = 10;
                soulsMax = 40;
            }
            return true;
        }
        return false;
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Soul Siphon",
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/portal.png"),
            hotkey,
            () -> active,
            () -> rank == 0,
            () -> true,
            () -> sendUnitCommand(UnitAction.TOGGLE_SOUL_SIPHON_PASSIVE),
            null,
            getTooltipLines(),
            this
        );
    }

    @Override
    public Button getRankUpButton() {
        return super.getRankUpButtonProtected(
            "Soul Siphon",
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/portal.png")
        );
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.soul_siphon") + " " + rankString(), true),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip3", soulsPerCast, soulsMax)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip4"))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.soul_siphon"), true),
                fcs(I18n.get("abilities.reignofnether.level_req", getLevelRequirement())),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip2")),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank1"), rank == 0),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank2"), rank == 1),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank3"), rank == 2)
        );
    }

    // returns amount of souls consumed
    public int consumeSouls() {
        if (active && souls > 0) {
            int soulsConsumed = Math.min(soulsPerCast, souls);
            souls -= soulsConsumed;
            return soulsConsumed;
        }
        return 0;
    }

    public void checkAndGainSouls(LivingEntity entityKilled) {
        if (active && souls > 0) {
            if (entityKilled instanceof Unit unit)
                souls += unit.getCost().population;
            else
                souls += 1;
        }
        if (souls > soulsMax)
            souls = soulsMax;
    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        active = !active;
    }
}
