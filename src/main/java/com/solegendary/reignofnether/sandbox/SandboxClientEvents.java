package com.solegendary.reignofnether.sandbox;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.buildings.neutral.EndPortal;
import com.solegendary.reignofnether.building.buildings.neutral.HealingFountain;
import com.solegendary.reignofnether.building.buildings.neutral.CapturableBeacon;
import com.solegendary.reignofnether.building.buildings.neutral.NeutralTransportPortal;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.gamemode.ClientGameModeHelper;
import com.solegendary.reignofnether.gamemode.GameMode;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.solegendary.reignofnether.player.PlayerClientEvents;
import com.solegendary.reignofnether.player.PlayerServerboundPacket;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.research.ResearchServerboundPacket;
import com.solegendary.reignofnether.unit.Relationship;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.units.monsters.*;
import com.solegendary.reignofnether.unit.units.neutral.*;
import com.solegendary.reignofnether.unit.units.piglins.*;
import com.solegendary.reignofnether.unit.units.villagers.*;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class SandboxClientEvents {

    // NONE == neutral
    private static Faction faction = Faction.NONE;

    public static Relationship relationship = Relationship.OWNED;

    public static SandboxMenuType sandboxMenuType = SandboxMenuType.BUILDINGS;

    private static final Minecraft MC = Minecraft.getInstance();

    public static Faction getFaction() { return faction; }

    public static String spawnUnitName = "";

    public static boolean isSandboxPlayer(String playerName) {
        return MC.player != null && playerName.equals(MC.player.getName().getString()) &&
                PlayerClientEvents.isRTSPlayer && ClientGameModeHelper.gameMode == GameMode.SANDBOX;
    }

    public static boolean isSandboxPlayer() {
        return PlayerClientEvents.isRTSPlayer && ClientGameModeHelper.gameMode == GameMode.SANDBOX;
    }

    public static List<AbilityButton> getNeutralBuildingButtons() {
        return List.of(
            CapturableBeacon.getBuildButton(Keybindings.keyQ),
            HealingFountain.getBuildButton(Keybindings.keyW),
            EndPortal.getBuildButton(Keybindings.keyE),
            NeutralTransportPortal.getBuildButton(Keybindings.keyR)
        );
    }

    public static List<AbilityButton> getBuildingButtons() {
        return switch (faction) {
            case VILLAGERS -> VillagerUnit.getBuildingButtons();
            case MONSTERS -> ZombieVillagerUnit.getBuildingButtons();
            case PIGLINS -> GruntUnit.getBuildingButtons();
            case NONE -> getNeutralBuildingButtons();
        };
    }

    public static List<AbilityButton> getUnitButtons() {
        return switch (faction) {
            case VILLAGERS -> List.of(
                VillagerProd.getPlaceButton(),
                VindicatorProd.getPlaceButton(),
                PillagerProd.getPlaceButton(),
                IronGolemProd.getPlaceButton(),
                WitchProd.getPlaceButton(),
                EvokerProd.getPlaceButton(),
                RavagerProd.getPlaceButton(),
                RoyalGuardProd.getPlaceButton()
            );
            case MONSTERS -> List.of(
                ZombieVillagerProd.getPlaceButton(),
                ZombieProd.getPlaceButton(),
                DrownedProd.getPlaceButton(),
                HuskProd.getPlaceButton(),
                SkeletonProd.getPlaceButton(),
                StrayProd.getPlaceButton(),
                SpiderProd.getPlaceButton(),
                PoisonSpiderProd.getPlaceButton(),
                CreeperProd.getPlaceButton(),
                SlimeProd.getPlaceButton(),
                WardenProd.getPlaceButton(),
                NecromancerProd.getPlaceButton(),
                ZombiePiglinProd.getPlaceButton(),
                ZoglinProd.getPlaceButton()
            );
            case PIGLINS -> List.of(
                GruntProd.getPlaceButton(),
                BruteProd.getPlaceButton(),
                HeadhunterProd.getPlaceButton(),
                HoglinProd.getPlaceButton(),
                BlazeProd.getPlaceButton(),
                WitherSkeletonProd.getPlaceButton(),
                MagmaCubeProd.getPlaceButton(),
                GhastProd.getPlaceButton(),
                PiglinMerchantProd.getPlaceButton()
            );
            case NONE -> List.of(
                EndermanProd.getPlaceButton(),
                PolarBearProd.getPlaceButton(),
                GrizzlyBearProd.getPlaceButton(),
                PandaProd.getPlaceButton(),
                WolfProd.getPlaceButton(),
                LlamaProd.getPlaceButton()
            );
        };
    }

    private static String getFactionName() {
        return switch (faction) {
            case VILLAGERS -> I18n.get("hud.faction.reignofnether.villager");
            case MONSTERS -> I18n.get("hud.faction.reignofnether.monster");
            case PIGLINS -> I18n.get("hud.faction.reignofnether.piglin");
            case NONE -> I18n.get("hud.faction.reignofnether.neutral");
        };
    }

    private static String getRelationshipName() {
        return switch (relationship) {
            case OWNED -> I18n.get("hud.relationship.reignofnether.owned");
            case FRIENDLY -> I18n.get("hud.relationship.reignofnether.allied");
            case NEUTRAL -> I18n.get("hud.relationship.reignofnether.neutral");
            case HOSTILE -> I18n.get("hud.relationship.reignofnether.enemy");
        };
    }

    public static Button getToggleFactionButton() {
        return new Button(
                "Toggle Faction",
                Button.itemIconSize,
                switch (faction) {
                    case VILLAGERS -> new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/villager.png");
                    case MONSTERS -> new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/creeper.png");
                    case PIGLINS -> new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/grunt.png");
                    case NONE -> new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/sheep.png");
                },
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> {
                    switch (faction) {
                        case VILLAGERS -> faction = Faction.MONSTERS;
                        case MONSTERS -> faction = Faction.PIGLINS;
                        case PIGLINS -> faction = Faction.NONE;
                        case NONE -> faction = Faction.VILLAGERS;
                    }
                },
                () -> {
                    switch (faction) {
                        case VILLAGERS -> faction = Faction.NONE;
                        case MONSTERS -> faction = Faction.VILLAGERS;
                        case PIGLINS -> faction = Faction.MONSTERS;
                        case NONE -> faction = Faction.PIGLINS;
                    }
                },
                List.of(
                        fcs(I18n.get("hud.faction.reignofnether.villager"), faction == Faction.VILLAGERS),
                        fcs(I18n.get("hud.faction.reignofnether.monster"), faction == Faction.MONSTERS),
                        fcs(I18n.get("hud.faction.reignofnether.piglin"), faction == Faction.PIGLINS),
                        fcs(I18n.get("hud.faction.reignofnether.neutral"), faction == Faction.NONE),
                        fcs(I18n.get("sandbox.reignofnether.faction_button2"))
                )
        );
    }

    public static Button getToggleRelationshipButton() {
        return new Button(
                "Toggle Relationship",
                Button.itemIconSize,
                switch (relationship) {
                    case OWNED -> new ResourceLocation("minecraft", "textures/block/lime_wool.png");
                    case FRIENDLY -> new ResourceLocation("minecraft", "textures/block/blue_wool.png");
                    case NEUTRAL -> new ResourceLocation("minecraft", "textures/block/yellow_wool.png");
                    case HOSTILE -> new ResourceLocation("minecraft", "textures/block/red_wool.png");
                },
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> {
                    switch (relationship) {
                        default -> relationship = Relationship.NEUTRAL;
                        case NEUTRAL -> relationship = Relationship.HOSTILE;
                        case HOSTILE -> relationship = Relationship.OWNED;
                    }
                },
                () -> {
                    switch (relationship) {
                        default -> relationship = Relationship.HOSTILE;
                        case NEUTRAL -> relationship = Relationship.OWNED;
                        case HOSTILE -> relationship = Relationship.NEUTRAL;
                    }
                },
                List.of(
                        fcs(I18n.get("sandbox.reignofnether.relationship_button1", getRelationshipName())),
                        fcs(I18n.get("sandbox.reignofnether.relationship_button2"))
                )
        );
    }

    public static Button getToggleBuildingOrUnitsButton() {
        return new Button(
                "Toggle Building or Units",
                Button.itemIconSize,
                switch (sandboxMenuType) {
                    case BUILDINGS -> new ResourceLocation("minecraft", "textures/block/crafting_table_front.png");
                    case UNITS -> new ResourceLocation("minecraft", "textures/item/spawn_egg.png");
                    case OTHER -> new ResourceLocation("minecraft", "textures/item/spawn_egg.png");
                },
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> {
                    switch (sandboxMenuType) {
                        case BUILDINGS -> sandboxMenuType = SandboxMenuType.UNITS;
                        case UNITS -> sandboxMenuType = SandboxMenuType.BUILDINGS;
                        case OTHER -> sandboxMenuType = SandboxMenuType.BUILDINGS;
                    }
                },
                null,
                List.of(
                        switch (sandboxMenuType) {
                            case BUILDINGS -> fcs(I18n.get("sandbox.reignofnether.menu_type_button_buildings"));
                            case UNITS -> fcs(I18n.get("sandbox.reignofnether.menu_type_button_units"));
                            case OTHER -> fcs(I18n.get("sandbox.reignofnether.menu_type_button_other"));
                        },
                        fcs(I18n.get("sandbox.reignofnether.menu_type_button1"))
                )
        );
    }

    public static Button getToggleBuildingCheatsButton() {
        Minecraft MC = Minecraft.getInstance();
        if (MC.player == null)
            return null;
        boolean hasCheats = ResearchClient.hasCheat("warpten") &&
                            ResearchClient.hasCheat("modifythephasevariance");
        String playerName = Minecraft.getInstance().player.getName().getString();
        return new Button(
                "Toggle Building Cheats",
                Button.itemIconSize,
                hasCheats ?
                        new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/command_block_side.png") :
                        new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/command_block_side_dark.png"),
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> {
                    if (hasCheats) {
                        ResearchServerboundPacket.removeCheat(playerName, "warpten");
                        ResearchServerboundPacket.removeCheat(playerName, "modifythephasevariance");
                    } else {
                        ResearchServerboundPacket.addCheat(playerName, "warpten");
                        ResearchServerboundPacket.addCheat(playerName, "modifythephasevariance");
                    }
                },
                null,
                List.of(hasCheats ? fcs(I18n.get("sandbox.reignofnether.building_cheats_on")) :
                                    fcs(I18n.get("sandbox.reignofnether.building_cheats_off")),
                                    fcs(I18n.get("sandbox.reignofnether.building_cheats1")),
                                    fcs(I18n.get("sandbox.reignofnether.building_cheats2"))
                )
        );
    }

    public static Button getToggleUnitCheatsButton() {
        Minecraft MC = Minecraft.getInstance();
        if (MC.player == null)
            return null;
        boolean hasCheats = ResearchClient.hasCheat("operationcwal") &&
                            ResearchClient.hasCheat("medievalman") &&
                            ResearchClient.hasCheat("foodforthought") &&
                            ResearchClient.hasCheat("slipslopslap");
        String playerName = Minecraft.getInstance().player.getName().getString();
        return new Button(
                "Toggle Unit Cheats",
                Button.itemIconSize,
                hasCheats ?
                        new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/chain_command_block_side.png") :
                        new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/chain_command_block_side_dark.png"),
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> {
                    if (hasCheats) {
                        ResearchServerboundPacket.removeCheat(playerName, "operationcwal");
                        ResearchServerboundPacket.removeCheat(playerName, "medievalman");
                        ResearchServerboundPacket.removeCheat(playerName, "foodforthought");
                        ResearchServerboundPacket.removeCheat(playerName, "slipslopslap");
                    } else {
                        ResearchServerboundPacket.addCheat(playerName, "operationcwal");
                        ResearchServerboundPacket.addCheat(playerName, "medievalman");
                        ResearchServerboundPacket.addCheat(playerName, "foodforthought");
                        ResearchServerboundPacket.addCheat(playerName, "slipslopslap");
                    }
                },
                null,
                List.of(hasCheats ? fcs(I18n.get("sandbox.reignofnether.unit_cheats_on")) :
                                    fcs(I18n.get("sandbox.reignofnether.unit_cheats_off")),
                                    fcs(I18n.get("sandbox.reignofnether.unit_cheats1")),
                                    fcs(I18n.get("sandbox.reignofnether.unit_cheats2")),
                                    fcs(I18n.get("sandbox.reignofnether.unit_cheats3"))
                )
        );
    }

    public static Button getExitSandboxButton() {
        return new Button(
                "Exit Sandbox Mode",
                Button.itemIconSize,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/cross.png"),
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                PlayerServerboundPacket::resetRTS,
                null,
                List.of(
                    fcs(I18n.get("sandbox.reignofnether.exit1")),
                    fcs(I18n.get("sandbox.reignofnether.exit2"))
                )
        );
    }

    @SubscribeEvent
    public static void onMouseClick(ScreenEvent.MouseButtonPressed.Post evt) {
        if (!OrthoviewClientEvents.isEnabled()) return;

        // prevent clicking behind HUDs
        if (HudClientEvents.isMouseOverAnyButtonOrHud() || MC.player == null) {
            CursorClientEvents.setLeftClickSandboxAction(null);
            return;
        }

        SandboxAction sandboxAction = CursorClientEvents.getLeftClickSandboxAction();
        if (evt.getButton() == GLFW.GLFW_MOUSE_BUTTON_1 && sandboxAction != null) {

            String ownerName = switch (relationship) {
               case NEUTRAL -> "";
               case HOSTILE -> "Enemy";
               default -> MC.player.getName().getString();
            };

            int entityId = 0;
            if (!UnitClientEvents.getSelectedUnits().isEmpty())
                entityId = UnitClientEvents.getSelectedUnits().get(0).getId();

            switch (sandboxAction) {
                case SPAWN_UNIT -> SandboxServerboundPacket.spawnUnit(CursorClientEvents.getLeftClickSandboxAction(), ownerName, spawnUnitName, CursorClientEvents.getPreselectedBlockPos());
                case SET_ANCHOR -> SandboxServerboundPacket.setAnchor(CursorClientEvents.getPreselectedBlockPos(), entityId);
            }

            if (!Keybindings.shiftMod.isDown()) {
                spawnUnitName = "";
                CursorClientEvents.setLeftClickSandboxAction(null);
            }
        }
        if (evt.getButton() == GLFW.GLFW_MOUSE_BUTTON_2) {
            spawnUnitName = "";
            CursorClientEvents.setLeftClickSandboxAction(null);
        }
    }
}
