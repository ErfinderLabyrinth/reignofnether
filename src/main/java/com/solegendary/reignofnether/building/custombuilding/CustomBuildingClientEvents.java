package com.solegendary.reignofnether.building.custombuilding;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.blocks.RTSStructureBlockEntity;
import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.BuildingBlockData;
import com.solegendary.reignofnether.guiscreen.TopdownGui;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.hud.RectZone;
import com.solegendary.reignofnether.hud.playerdisplay.ObserverPlayerDisplay;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.MyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.capitaliseAndSpace;
import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class CustomBuildingClientEvents {

    // since every custom building has a different structure, we need to maintain a list of them here
    public static final ArrayList<CustomBuilding> customBuildings = new ArrayList<>();

    public static final ArrayList<BlockPos> rtsStructuresToRenderBB = new ArrayList<>();

    private static final Minecraft MC = Minecraft.getInstance();

    private static CustomBuilding customBuildingMenu = null;
    private static final ArrayList<Button> renderedButtons = new ArrayList<>();
    private static final ArrayList<RectZone> hudZones = new ArrayList<>();

    public static void setCustomBuildingMenu(CustomBuilding customBuilding) {
        if (customBuildingMenu != customBuilding)
            customBuildingMenu = customBuilding;
        else
            customBuildingMenu = null;
    }

    public static Building getCustomBuilding(String name) {
        for (CustomBuilding customBuilding : customBuildings)
            if (customBuilding.name.equals(name))
                return customBuilding;
        return null;
    }

    public static void registerCustomBuilding(String playerName, String name, Vec3i structureSize, CompoundTag structureNbt) {
        if (MC.player == null || (!playerName.isEmpty() && !MC.player.getName().getString().equals(playerName)))
            return;

        for (CustomBuilding customBuilding : customBuildings) {
            if (customBuilding.name.equals(name)) {
                MC.player.sendSystemMessage(Component.literal("ERROR (client): custom building '" + name + "' already exists"));
                return;
            }
        }
        ArrayList<BuildingBlock> blocks = BuildingBlockData.getBuildingBlocksFromNbt(structureNbt);
        Block portraitBlock = Blocks.COMMAND_BLOCK;
        for (BuildingBlock bb : blocks) {
            BlockState bs = bb.getBlockState();
            if (!bs.isAir() && bs.getFluidState().isEmpty()) {
                portraitBlock = bs.getBlock();
            }
        }
        CustomBuilding building = new CustomBuilding(name, structureSize, portraitBlock, structureNbt);
        customBuildings.add(building);
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent evt) throws NoSuchFieldException {
        if (evt.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS || MC.level == null) {
            return;
        }

        for (BlockPos bp : rtsStructuresToRenderBB) {
            if (MC.level.getBlockEntity(bp) instanceof RTSStructureBlockEntity be) {
                BlockPos pos = be.getStructurePos();
                Vec3i size = be.getStructureSize();
                MyRenderer.drawLineBox(evt.getPoseStack(),
                        new AABB(bp.getX() + pos.getX(),
                                bp.getY() + pos.getY(),
                                bp.getZ() + pos.getZ(),
                                bp.getX() + pos.getX() + size.getX(),
                                bp.getY() + pos.getY() + size.getY(),
                                bp.getZ() + pos.getZ() + size.getZ()),
                        1f, 1f, 1f, 1f);
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreen(ScreenEvent.Render.Post evt) {
        if (customBuildingMenu != null && MC.screen instanceof TopdownGui) {
            hudZones.clear();
            renderedButtons.clear();

            int blitX = 100;
            int blitY = 40;
            int width = 290;
            int height = 100;
            MyRenderer.renderFrameWithBg(evt.getGuiGraphics(), blitX, blitY, width, height, 0xA0000000);

            Button closeButton = new Button("Close Custom Building Menu",
                    Button.itemIconSize,
                    ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/hud/cross_square.png"),
                    (Keybinding) null,
                    () -> false,
                    () -> false,
                    () -> true,
                    () -> setCustomBuildingMenu(null),
                    null,
                    List.of()
            );
            closeButton.frameResource = null;
            closeButton.render(evt.getGuiGraphics(), blitX + width - Button.itemIconSize - 12, blitY + 4, evt.getMouseX(), evt.getMouseY());
            renderedButtons.add(closeButton);
            hudZones.add(new RectZone(blitX, blitY, blitX + width, blitY + height));

            evt.getGuiGraphics().drawString(
                    Minecraft.getInstance().font,
                    "Coming soon: building customisation options!",
                    blitX + 10, blitY + 10,
                    0xFFFFFFFF
            );
        }
    }

    public static boolean isMouseOverHud(int mouseX, int mouseY) {
        for (RectZone hudZone : hudZones)
            if (hudZone.isMouseOver(mouseX, mouseY))
                return true;
        return false;
    }

    @SubscribeEvent
    public static void onMousePress(ScreenEvent.MouseButtonPressed.Post evt) {
        for (Button button : renderedButtons) {
            if (evt.getButton() == GLFW.GLFW_MOUSE_BUTTON_1) {
                button.checkClicked((int) evt.getMouseX(), (int) evt.getMouseY(), true);
            } else if (evt.getButton() == GLFW.GLFW_MOUSE_BUTTON_2) {
                button.checkClicked((int) evt.getMouseX(), (int) evt.getMouseY(), false);
            }
        }
    }
}






