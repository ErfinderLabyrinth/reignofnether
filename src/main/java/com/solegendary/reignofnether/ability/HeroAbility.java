package com.solegendary.reignofnether.ability;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class HeroAbility extends Ability {

    // can be ranked up when the hero levels up
    // requires a HeroUnit to be passed

    public final HeroUnit hero;
    public int rank = 0; // 0 == not learnt
    public final int maxRank;

    public HeroAbility(HeroUnit hero, int maxRank, UnitAction action, int cooldownMax, float range, float radius, boolean canTargetEntities) {
        super(action, ((Entity) hero).getLevel(), cooldownMax, range, radius, canTargetEntities);
        this.hero = hero;
        this.maxRank = maxRank;
    }

    public void rankUp() {
        if (rank < maxRank && hero.getSkillPoints() > 0) {
            rank += 1;
            hero.setSkillPoints(hero.getSkillPoints() - 1);
        }
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of();
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of();
    }

    public AbilityButton getButton(Keybinding hotkey) {
        return null;
    }

    // rank up button for this specific ability
    public AbilityButton getRankUpButton(Keybinding hotkey, String name, ResourceLocation resourceLocation) {
        return new AbilityButton(name,
            resourceLocation,
            hotkey,
            () -> false,
            () -> hero.isRankUpMenuOpen() && rank < maxRank,
            () -> hero.getSkillPoints() > 0,
            () -> {
                AbilityServerboundPacket.rankUpAbility(((Entity) hero).getId(), action);
                hero.showRankUpMenu(true);
            },
            null,
            getRankUpTooltipLines(),
            this
        );
    }

    // button that all heroes have to show
    public AbilityButton getRankUpMenuButton() {
        return new AbilityButton("Rank up abilities",
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/tick.png"),
            Keybindings.keyU,
            () -> false,
            () -> hero.getSkillPoints() > 0,
            () -> true,
            () -> hero.showRankUpMenu(!hero.isRankUpMenuOpen()),
            null,
            getRankUpTooltipLines(),
            this
        );
    }
}
