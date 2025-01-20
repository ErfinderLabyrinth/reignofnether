package com.solegendary.reignofnether.gamemode;

import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.player.PlayerServerboundPacket;
import com.solegendary.reignofnether.survival.SurvivalClientEvents;
import com.solegendary.reignofnether.survival.WaveDifficulty;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class ClientGameModeHelper {

    public static GameMode DEFAULT_GAMEMODE = GameMode.CLASSIC;
    public static GameMode gameMode = DEFAULT_GAMEMODE;
    public static boolean gameModeLocked = false; // locked with startRTS() in any gamemode, unlocked with /rts-reset
    public static boolean disallowSurvival = false;

    public static void cycleGameMode() {
        if (gameModeLocked)
            return;
        switch (gameMode) {
            case CLASSIC -> {
                if (!disallowSurvival)
                    gameMode = GameMode.SURVIVAL;
                else
                    gameMode = GameMode.SANDBOX;
            }
            case SURVIVAL -> gameMode = GameMode.SANDBOX;
            default -> gameMode = GameMode.CLASSIC;
        }
    }

    public static void cycleWaveDifficulty() {
        switch (SurvivalClientEvents.difficulty) {
            case BEGINNER -> SurvivalClientEvents.difficulty = WaveDifficulty.EASY;
            case EASY -> SurvivalClientEvents.difficulty = WaveDifficulty.MEDIUM;
            case MEDIUM -> SurvivalClientEvents.difficulty = WaveDifficulty.HARD;
            case HARD -> SurvivalClientEvents.difficulty = WaveDifficulty.EXTREME;
            case EXTREME -> SurvivalClientEvents.difficulty = WaveDifficulty.BEGINNER;
        }
    }

    private static String getLockedString() {
        return gameModeLocked ? " " + I18n.get("hud.gamemode.reignofnether.locked") : "";
    }

    // all gamemodes are controlled by 1 button, cycled with right-click
    // left click provides functionality specific to the gamemode, eg. changing wave survival difficulty
    public static Button getButton() {
        Button button = switch (gameMode) {
            case CLASSIC -> new Button(
                    "Classic",
                    Button.itemIconSize,
                    new ResourceLocation("minecraft", "textures/block/grass_block_side.png"),
                    (Keybinding) null,
                    () -> false,
                    () -> false,
                    () -> !gameModeLocked,
                    null,
                    ClientGameModeHelper::cycleGameMode,
                    List.of(
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.classic1") +
                                    getLockedString(), Style.EMPTY.withBold(true)),
                            FormattedCharSequence.forward("", Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.classic2"), Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.classic3"), Style.EMPTY),
                            FormattedCharSequence.forward("", Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.changemode"), Style.EMPTY)
                    )
            );
            case SURVIVAL -> new Button(
                    "Survival",
                    Button.itemIconSize,
                    switch (SurvivalClientEvents.difficulty) {
                        case BEGINNER -> new ResourceLocation("minecraft", "textures/item/wooden_sword.png");
                        case EASY -> new ResourceLocation("minecraft", "textures/item/stone_sword.png");
                        case MEDIUM -> new ResourceLocation("minecraft", "textures/item/iron_sword.png");
                        case HARD -> new ResourceLocation("minecraft", "textures/item/diamond_sword.png");
                        case EXTREME -> new ResourceLocation("minecraft", "textures/item/netherite_sword.png");
                    },
                    (Keybinding) null,
                    () -> false,
                    () -> false,
                    () -> !gameModeLocked,
                    ClientGameModeHelper::cycleWaveDifficulty,
                    ClientGameModeHelper::cycleGameMode,
                    List.of(
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.survival1") +
                                    getLockedString(), Style.EMPTY.withBold(true)),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.survival4",
                                    SurvivalClientEvents.difficulty, SurvivalClientEvents.getMinutesPerDay()), Style.EMPTY),
                            FormattedCharSequence.forward("", Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.survival2"), Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.survival3"), Style.EMPTY),
                            FormattedCharSequence.forward("", Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.survival5"), Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.changemode"), Style.EMPTY)
                    )
            );
            case SANDBOX -> new Button(
                    "Sandbox",
                    Button.itemIconSize,
                    new ResourceLocation("minecraft", "textures/block/crafting_table_front.png"),
                    (Keybinding) null,
                    () -> false,
                    () -> false,
                    () -> !gameModeLocked,
                    null,
                    ClientGameModeHelper::cycleGameMode,
                    List.of(
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.sandbox1") +
                                    getLockedString(), Style.EMPTY.withBold(true)),
                            FormattedCharSequence.forward("", Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.sandbox2"), Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.sandbox3"), Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.sandbox4"), Style.EMPTY),
                            FormattedCharSequence.forward("", Style.EMPTY),
                            FormattedCharSequence.forward(I18n.get("hud.gamemode.reignofnether.changemode"), Style.EMPTY)
                    )
            );
            default -> null;
        };
        if (button != null)
            button.tooltipOffsetY = 15;
        return button;
    }
}
