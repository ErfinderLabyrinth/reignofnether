package com.solegendary.reignofnether.gamerules;

import com.mojang.blaze3d.vertex.PoseStack;
import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.player.PlayerClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.unit.UnitAction;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class GameruleClient {

    public static boolean gamerulesMenuOpen = false;

    public static Button getGamerulesButton() {
        return new Button(
            "Game Rules Menu",
            14,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/repeating_command_block_back.png"),
            (Keybinding) null,
            () -> false,
            () -> !PlayerClientEvents.isRTSPlayer,
            () -> true,
            () -> gamerulesMenuOpen = !gamerulesMenuOpen,
            null,
            List.of(
                fcs(I18n.get("Game Rules Menu"))
            )
        );
    }

    // returns list of rendered buttons
    public static List<Button> renderGamerulesGUI(PoseStack poseStack, int xTR, int yTR) {
        return List.of();
    }
}