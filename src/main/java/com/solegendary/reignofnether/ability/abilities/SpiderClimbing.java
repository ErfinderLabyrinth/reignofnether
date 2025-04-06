package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.SpiderUnit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class SpiderClimbing extends Ability {

    public SpiderClimbing() {
        super(
            UnitAction.TOGGLE_SPIDER_CLIMBING,
            0,
            0,
            0,
            false,
            false
        );
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey, Unit unit) {
        SpiderUnit spider = (SpiderUnit) unit;
        ResourceLocation rlLadder = new ResourceLocation("minecraft", "textures/block/ladder.png");
        ResourceLocation rlBarrier = new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/items/barrier.png");

        AbilityButton ab = new AbilityButton(
                "Toggle Wall Climbing",
                spider.isWallClimbing() ? rlLadder : rlBarrier,
                hotkey,
                () -> false,
                () -> false,
                () -> true,
                () -> UnitClientEvents.sendUnitCommand(UnitAction.TOGGLE_SPIDER_CLIMBING),
                null,
                List.of(
                        spider.isWallClimbing() ?
                        fcs(I18n.get("abilities.reignofnether.spider_climbing_on")) :
                        fcs(I18n.get("abilities.reignofnether.spider_climbing_off"))
                ),
                this
        );
        if (!spider.isWallClimbing())
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
