package com.solegendary.reignofnether.ability.heroAbilities.monster;

//Forces day to night under a blood red moon for the entire world temporarily
//Raises the movement and attack speed of all of your units while active (other monster players' units are unaffected)
//Soul Siphon extends the duration

// play a cave sound and announce "A blood moon rises" in global chat

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.NecromancerUnit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.unit.UnitClientEvents.sendUnitCommand;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;
import static com.solegendary.reignofnether.util.MiscUtil.fcsIcons;

public class BloodMoon extends HeroAbility {

    public static final int CHANNEL_TICKS = 40;
    private static final int CD_MAX_SECONDS = 360 * ResourceCost.TICKS_PER_SECOND;
    private static final int DURATION_SECONDS = 60;
    private static final int BONUS_SECONDS_PER_SOUL = 3;

    public BloodMoon(HeroUnit hero) {
        super(hero, 1, UnitAction.BLOOD_MOON, CD_MAX_SECONDS, 0, 0, false);
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton("Blood Moon",
            new ResourceLocation("minecraft", "textures/block/redstone_block.png"),
            hotkey,
            () -> false,
            () -> rank == 0,
            () -> true,
            () -> sendUnitCommand(UnitAction.BLOOD_MOON),
            null,
            getTooltipLines(),
            this
        );
    }

    @Override
    public Button getRankUpButton() {
        return super.getRankUpButtonProtected(
            "Blood Moon",
            new ResourceLocation("minecraft", "textures/block/redstone_block.png")
        );
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.blood_moon"), true),
                fcsIcons(I18n.get("abilities.reignofnether.blood_moon.stats", CD_MAX_SECONDS / 20)),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip3")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip4", DURATION_SECONDS, BONUS_SECONDS_PER_SOUL))
        );
    }

    public List<FormattedCharSequence> getRankUpTooltipLines() {
        return List.of(
                fcs(I18n.get("abilities.reignofnether.blood_moon"), true),
                fcs(I18n.get("abilities.reignofnether.level_req", getLevelRequirement()), getLevelReqStyle()),
                fcs(""),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip1")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip2")),
                fcs(I18n.get("abilities.reignofnether.blood_moon.tooltip3"))
        );
    }

    @Override
    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        ((NecromancerUnit) unitUsing).getCastBloodMoonGoal().setAbility(this);
        ((NecromancerUnit) unitUsing).getCastBloodMoonGoal().startCasting();
    }

    @Override
    public void use(Level level, Unit unitUsing, LivingEntity targetEntity) {
        ((NecromancerUnit) unitUsing).getCastBloodMoonGoal().setAbility(this);
        ((NecromancerUnit) unitUsing).getCastBloodMoonGoal().startCasting();
    }
}
