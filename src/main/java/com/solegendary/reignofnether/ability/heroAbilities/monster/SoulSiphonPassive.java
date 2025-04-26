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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class SoulSiphonPassive extends HeroAbility {

    public int souls = 0;
    public int soulsPerCast = 4;
    public int soulsMax = 20;

    public SoulSiphonPassive() {
        super(3, UnitAction.NONE, 0, 0, 0, false);
        this.autocastEnableAction = UnitAction.ENABLE_SOUL_SIPHON_PASSIVE;
        this.autocastDisableAction = UnitAction.DISBLE_SOUL_SIPHON_PASSIVE;
    }

    public boolean rankUp(HeroUnit hero) {
        if (super.rankUp(hero)) {
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
    public AbilityButton getButton(Keybinding hotkey, HeroUnit hero) {
        return new AbilityButton("Soul Siphon",
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/portal.png"),
            hotkey,
            this::getAutocast,
            () -> rank == 0,
            () -> true,
            this::toggleAutocast,
            null,
            getTooltipLines(hero),
            this
        );
    }

    @Override
    public Button getRankUpButton(HeroUnit hero) {
        return super.getRankUpButtonProtected(
                "Soul Siphon",
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/portal.png"),
                hero
        );
    }

    public List<FormattedCharSequence> getTooltipLines(HeroUnit hero) {
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

    public List<FormattedCharSequence> getRankUpTooltipLines(HeroUnit hero) {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.soul_siphon"), true),
                fcs(I18n.get("abilities.reignofnether.level_req", getLevelRequirement()), getLevelReqStyle(hero)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.tooltip3", soulsPerCast, soulsMax)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.can_be_toggled")),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank1"), rank == 0),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank2"), rank == 1),
                fcs(I18n.get("abilities.reignofnether.soul_siphon.rank3"), rank == 2)
        );
    }

    // returns amount of souls consumed
    public int consumeSouls() {
        if (getAutocast() && souls > 0) {
            int soulsConsumed = Math.min(soulsPerCast, souls);
            souls -= soulsConsumed;
            return soulsConsumed;
        }
        return 0;
    }

    public void checkAndGainSouls(LivingEntity entityKilled) {
        if (getAutocast() && souls > 0) {
            if (entityKilled instanceof Unit unit)
                souls += unit.getCost().population;
            else
                souls += 1;
        }
        if (souls > soulsMax)
            souls = soulsMax;
    }
}
