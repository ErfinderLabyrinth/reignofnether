package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.research.researchItems.ResearchBloodlust;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.SpiderUnit;
import com.solegendary.reignofnether.unit.units.piglins.BruteUnit;
import com.solegendary.reignofnether.unit.units.piglins.HeadhunterUnit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class SpiderClimbing extends Ability {

    private final SpiderUnit unit;

    public SpiderClimbing(SpiderUnit unit) {
        super(
            UnitAction.TOGGLE_SPIDER_CLIMBING,
            unit.level(),
            0,
            0,
            0,
            false,
            false
        );
        this.unit = unit;
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        ResourceLocation rlLadder = new ResourceLocation("minecraft", "textures/block/ladder.png");
        ResourceLocation rlBarrier = new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/items/barrier.png");

        AbilityButton ab = new AbilityButton(
                "Toggle Wall Climbing",
                unit.isWallClimbing() ? rlLadder : rlBarrier,
                hotkey,
                () -> false,
                () -> false,
                () -> true,
                () -> UnitClientEvents.sendUnitCommand(UnitAction.TOGGLE_SPIDER_CLIMBING),
                null,
                List.of(
                    unit.isWallClimbing() ?
                        fcs(I18n.get("abilities.reignofnether.spider_climbing_on")) :
                        fcs(I18n.get("abilities.reignofnether.spider_climbing_off"))
                ),
                this
        );
        if (!unit.isWallClimbing())
            ab.bgIconResource = rlLadder;

        return ab;
    }

    @Override
    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        if (unitUsing instanceof SpiderUnit spiderUnit)
            spiderUnit.toggleWallClimbing();
        if (level.isClientSide())
            unitUsing.updateAbilityButtons();
    }

    @Override
    public boolean shouldResetBehaviours() { return false; }
}
