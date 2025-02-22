package com.solegendary.reignofnether.hud.buttons;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.player.PlayerClientEvents;
import com.solegendary.reignofnether.player.PlayerServerboundPacket;
import com.solegendary.reignofnether.startpos.StartPos;
import com.solegendary.reignofnether.startpos.StartPosClientEvents;
import com.solegendary.reignofnether.startpos.StartPosServerboundPacket;
import com.solegendary.reignofnether.tutorial.TutorialClientEvents;
import com.solegendary.reignofnether.tutorial.TutorialStage;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class StartButtons {

    public static final int ICON_SIZE = 14;

    private static final Minecraft MC = Minecraft.getInstance();

    public static Button sandboxStartButton = new Button(
            "Sandbox",
            ICON_SIZE,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/tick.png"),
            (Keybinding) null,
            () -> false,
            () -> false,
            () -> true,
            () -> PlayerServerboundPacket.startRTS(Faction.NONE, 0d,0d,0d),
            null,
            List.of(
                    fcs(I18n.get("hud.gamemode.reignofnether.sandbox_confirm"))
            )
    );

    public static Button villagerStartButton = new Button(
        "Villagers",
            ICON_SIZE,
        new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/villager.png"),
        (Keybinding) null,
        () -> CursorClientEvents.getLeftClickAction() == UnitAction.STARTRTS_VILLAGERS,
        () -> !TutorialClientEvents.isAtOrPastStage(TutorialStage.PLACE_WORKERS_B) || !PlayerClientEvents.canStartRTS,
        () -> true,
        () -> {
            CursorClientEvents.setLeftClickAction(UnitAction.STARTRTS_VILLAGERS);
        },
        null,
        List.of(
            fcs(I18n.get("hud.startbuttons.villagers.reignofnether.first"), true),
            fcs(I18n.get("hud.startbuttons.villagers.reignofnether.second"))
        )
    );

    public static Button monsterStartButton = new Button(
        "Monsters",
            ICON_SIZE,
        new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/creeper.png"),
        (Keybinding) null,
        () -> CursorClientEvents.getLeftClickAction() == UnitAction.STARTRTS_MONSTERS,
        () -> TutorialClientEvents.isEnabled() || !PlayerClientEvents.canStartRTS,
        () -> !TutorialClientEvents.isEnabled(),
        () -> CursorClientEvents.setLeftClickAction(UnitAction.STARTRTS_MONSTERS),
        null,
        List.of(
            fcs(I18n.get("hud.startbuttons.monsters.reignofnether.first"), true),
            fcs(I18n.get("hud.startbuttons.monsters.reignofnether.second"))
        )
    );

    public static Button piglinStartButton = new Button(
        "Piglins",
        ICON_SIZE,
        new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/grunt.png"),
        (Keybinding) null,
        () -> CursorClientEvents.getLeftClickAction() == UnitAction.STARTRTS_PIGLINS,
        () -> TutorialClientEvents.isEnabled() || !PlayerClientEvents.canStartRTS,
        () -> !TutorialClientEvents.isEnabled(),
        () -> CursorClientEvents.setLeftClickAction(UnitAction.STARTRTS_PIGLINS),
        null,
        List.of(
            fcs(I18n.get("hud.startbuttons.piglins.reignofnether.first"), true),
            fcs(I18n.get("hud.startbuttons.piglins.reignofnether.second"))
        )
    );

    public static Button villagerReadyButton = new Button(
            "Villagers",
            ICON_SIZE,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/villager.png"),
            (Keybinding) null,
            () -> StartPosClientEvents.selectedFaction == Faction.VILLAGERS,
            () -> TutorialClientEvents.isEnabled() || !PlayerClientEvents.canStartRTS || !StartPosClientEvents.isEnabled(),
            () -> !StartPosClientEvents.isSelectedPosReservedByOther(),
            () -> {
                StartPos startPos = StartPosClientEvents.getPos();
                if (startPos != null && MC.player != null) {
                    if (StartPosClientEvents.selectedFaction != Faction.VILLAGERS) {
                        StartPosClientEvents.selectedFaction = Faction.VILLAGERS;
                        StartPosServerboundPacket.reservePos(startPos.pos, Faction.VILLAGERS, MC.player.getName().getString());
                    } else {
                        StartPosClientEvents.selectedFaction = Faction.NONE;
                        StartPosServerboundPacket.unreservePos(StartPosClientEvents.getPos().pos);
                    }
                }
            },
            null,
            List.of(
                    fcs(I18n.get("hud.startbuttons.villagers.reignofnether.first_startpos"), true),
                    fcs(I18n.get("hud.startbuttons.villagers.reignofnether.second_startpos"))
            )
    );

    public static Button monsterReadyButton = new Button(
            "Monsters",
            ICON_SIZE,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/creeper.png"),
            (Keybinding) null,
            () -> StartPosClientEvents.selectedFaction == Faction.MONSTERS,
            () -> TutorialClientEvents.isEnabled() || !PlayerClientEvents.canStartRTS || !StartPosClientEvents.isEnabled(),
            () -> !StartPosClientEvents.isSelectedPosReservedByOther(),
            () -> {
                StartPos startPos = StartPosClientEvents.getPos();
                if (startPos != null && MC.player != null) {
                    if (StartPosClientEvents.selectedFaction != Faction.MONSTERS) {
                        StartPosClientEvents.selectedFaction = Faction.MONSTERS;
                        StartPosServerboundPacket.reservePos(startPos.pos, Faction.MONSTERS, MC.player.getName().getString());
                    } else {
                        StartPosClientEvents.selectedFaction = Faction.NONE;
                        StartPosServerboundPacket.unreservePos(StartPosClientEvents.getPos().pos);
                    }
                }
            },
            null,
            List.of(
                    fcs(I18n.get("hud.startbuttons.monsters.reignofnether.first_startpos"), true),
                    fcs(I18n.get("hud.startbuttons.monsters.reignofnether.second_startpos"))
            )
    );

    public static Button piglinReadyButton = new Button(
            "Piglins",
            ICON_SIZE,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/grunt.png"),
            (Keybinding) null,
            () -> StartPosClientEvents.selectedFaction == Faction.PIGLINS,
            () -> TutorialClientEvents.isEnabled() || !PlayerClientEvents.canStartRTS || !StartPosClientEvents.isEnabled(),
            () -> !StartPosClientEvents.isSelectedPosReservedByOther(),
            () -> {
                StartPos startPos = StartPosClientEvents.getPos();
                if (startPos != null && MC.player != null) {
                    if (StartPosClientEvents.selectedFaction != Faction.PIGLINS) {
                        StartPosClientEvents.selectedFaction = Faction.PIGLINS;
                        StartPosServerboundPacket.reservePos(startPos.pos, Faction.PIGLINS, MC.player.getName().getString());
                    } else {
                        StartPosClientEvents.selectedFaction = Faction.NONE;
                        StartPosServerboundPacket.unreservePos(StartPosClientEvents.getPos().pos);
                    }
                }
            },
            null,
            List.of(
                fcs(I18n.get("hud.startbuttons.piglins.reignofnether.first_startpos"), true),
                fcs(I18n.get("hud.startbuttons.piglins.reignofnether.second_startpos"))
            )
    );
}
