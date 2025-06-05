package com.solegendary.reignofnether.ability.heroAbilities.villager;

//Temporarily gain bonus damage, health and movespeed
//While active, the guard's model becomes larger and has a shimmering enchanted effect
// Duration is extended whenever damage is taken from an enemy

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.villagers.RoyalGuardUnit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;
import static com.solegendary.reignofnether.util.MiscUtil.fcsIcons;

public class Avatar extends HeroAbility {

    private static final int CD_MAX_SECONDS = 300 * ResourceCost.TICKS_PER_SECOND;
    private static final int DURATION = 60 * ResourceCost.TICKS_PER_SECOND;

    private static final float BONUS_HEALTH = 150;

    public Avatar(HeroUnit hero) {
        super(hero, 1, UnitAction.AVATAR, CD_MAX_SECONDS, 0, 0, false);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Avatar",
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/vindicator.png"),
                hotkey,
                () -> false,
                () -> rank == 0,
                () -> true,
                () -> sendUnitCommand(UnitAction.AVATAR),
                null,
                getTooltipLines(),
                this
        );
    }

    @Override
    public Button getRankUpButton() {
        return super.getRankUpButtonProtected(
                "Avatar",
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/vindicator.png")
        );
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.avatar"), true),
                fcsIcons(I18n.get("abilities.reignofnether.avatar.stats", CD_MAX_SECONDS / 20)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.avatar.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.avatar.tooltip2", BONUS_HEALTH)),
                fcs(I18n.get("abilities.reignofnether.avatar.tooltip3", DURATION / 20))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.avatar"), true),
                fcs(I18n.get("abilities.reignofnether.level_req", getLevelRequirement()), getLevelReqStyle()),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.avatar.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.avatar.tooltip2", BONUS_HEALTH)),
                fcs(I18n.get("abilities.reignofnether.avatar.tooltip3", DURATION / 20))
        );
    }

    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        ((RoyalGuardUnit) unitUsing).avatar();
    }
}
