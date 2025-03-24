package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.building.production.ProductionItems;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitAnimationAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.packets.UnitAnimationClientboundPacket;
import com.solegendary.reignofnether.unit.units.piglins.BruteUnit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class ToggleShield extends Ability {

    private static final int CD_MAX_SECONDS = 0;

    public ToggleShield() {
        super(
                UnitAction.TOGGLE_SHIELD,
                CD_MAX_SECONDS * ResourceCost.TICKS_PER_SECOND,
                0,
                0,
                false
        );
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey, Unit unit) {
        BruteUnit bruteUnit = (BruteUnit) unit;
        return new AbilityButton(
                "Shield Stance",
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/items/shield.png"),
                hotkey,
                () -> bruteUnit.isHoldingUpShield,
                () -> !ResearchClient.hasResearch(ProductionItems.RESEARCH_BRUTE_SHIELDS) ||
                        bruteUnit.getItemBySlot(EquipmentSlot.OFFHAND).getItem() != Items.SHIELD,
                () -> true,
                () -> UnitClientEvents.sendUnitCommand(UnitAction.TOGGLE_SHIELD),
                null,
                List.of(
                        fcs(I18n.get("abilities.reignofnether.shield_stance"), true),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("abilities.reignofnether.shield_stance.tooltip1"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("abilities.reignofnether.shield_stance.tooltip2"), Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("abilities.reignofnether.shield_stance.tooltip3"), Style.EMPTY)
                ),
                this
        );
    }

    @Override
    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        BruteUnit bruteUnit = (BruteUnit) unitUsing;
        bruteUnit.isHoldingUpShield = !bruteUnit.isHoldingUpShield;
        if (!level.isClientSide()) {
            if (bruteUnit.isHoldingUpShield)
                UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.NON_KEYFRAME_START, bruteUnit);
            else
                UnitAnimationClientboundPacket.sendBasicPacket(UnitAnimationAction.NON_KEYFRAME_STOP, bruteUnit);

            BlockPos bp = unitUsing.getMoveGoal().getMoveTarget();
            unitUsing.getMoveGoal().stopMoving();
            unitUsing.getMoveGoal().setMoveTarget(bp);
        }
    }

    @Override
    public boolean shouldResetBehaviours() { return false; }
}
