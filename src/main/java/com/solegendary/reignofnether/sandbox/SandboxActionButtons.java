package com.solegendary.reignofnether.sandbox;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

// action buttons but only for neutral units in sandbox

public class SandboxActionButtons {

    private static boolean neutralUnitsSelected() {
        for (LivingEntity entity : UnitClientEvents.getSelectedUnits())
            if (entity instanceof Unit unit && !unit.getOwnerName().isBlank())
                return false;
        return true;
    }

    private static boolean selectedUnitsHaveAnchor() {
        for (LivingEntity entity : UnitClientEvents.getSelectedUnits())
            if (entity instanceof Unit unit && unit.getAnchor() != null && !unit.getAnchor().equals(new BlockPos(0,0,0)))
                return true;
        return false;
    }

    public static final Button SET_ANCHOR = new Button(
            "Set Anchor",
            Button.itemIconSize,
            new ResourceLocation("minecraft", "textures/block/respawn_anchor_side4.png"),
            Keybindings.keyQ,
            () -> CursorClientEvents.getLeftClickSandboxAction() == SandboxAction.SET_ANCHOR,
            () -> !SandboxClientEvents.isSandboxPlayer(),
            () -> true,
            () -> CursorClientEvents.setLeftClickSandboxAction(SandboxAction.SET_ANCHOR),
            null,
            List.of(
                    fcs(I18n.get("hud.actionbuttons.reignofnether.set_anchor"), true),
                    fcs(I18n.get("hud.actionbuttons.reignofnether.set_anchor.tooltip1"))
            )
    );

    public static final Button RESET_TO_ANCHOR = new Button(
            "Reset to Anchor",
            Button.itemIconSize,
            new ResourceLocation("minecraft", "textures/block/respawn_anchor_top_off.png"),
            Keybindings.keyW,
            () -> CursorClientEvents.getLeftClickSandboxAction() == SandboxAction.RESET_TO_ANCHOR,
            () -> !SandboxClientEvents.isSandboxPlayer(),
            () -> selectedUnitsHaveAnchor(),
            () -> {
                if (!UnitClientEvents.getSelectedUnits().isEmpty()) {
                    LivingEntity entity = UnitClientEvents.getSelectedUnits().get(0);
                    if (entity instanceof Unit unit) {
                        SandboxServerboundPacket.resetToAnchor(entity.getId());
                        Unit.fullResetBehaviours(unit);
                    }
                }
            },
            null,
            List.of(
                    fcs(I18n.get("hud.actionbuttons.reignofnether.reset_to_anchor"), true),
                    fcs(I18n.get("hud.actionbuttons.reignofnether.reset_to_anchor.tooltip1"))
            )
    );

    public static final Button REMOVE_ANCHOR = new Button(
            "Remove Anchor",
            Button.itemIconSize,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/items/barrier.png"),
            Keybindings.keyE,
            () -> CursorClientEvents.getLeftClickSandboxAction() == SandboxAction.REMOVE_ANCHOR,
            () -> !SandboxClientEvents.isSandboxPlayer(),
            () -> selectedUnitsHaveAnchor(),
            () -> {
                if (!UnitClientEvents.getSelectedUnits().isEmpty())
                    SandboxServerboundPacket.removeAnchor(UnitClientEvents.getSelectedUnits().get(0).getId());
            },
            null,
            List.of(
                    fcs(I18n.get("hud.actionbuttons.reignofnether.remove_anchor"), true)
            )
    );
}
