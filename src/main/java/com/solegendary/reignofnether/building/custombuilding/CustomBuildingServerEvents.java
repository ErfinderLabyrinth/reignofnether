package com.solegendary.reignofnether.building.custombuilding;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.blocks.RTSStructureBlockEntity;
import com.solegendary.reignofnether.building.*;
import com.solegendary.reignofnether.player.PlayerServerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CustomBuildingServerEvents {

    // since every custom building has a different structure, we need to maintain a list of them here
    private static final ArrayList<CustomBuilding> customBuildings = new ArrayList<>();

    public static Building getCustomBuilding(String name) {
        for (CustomBuilding building : customBuildings)
            if (building.name.equals(name))
                return building;
        return null;
    }

    public static boolean createAndRegisterNewCustomBuilding(ResourceLocation structureRL, String structureName, ServerLevel level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof RTSStructureBlockEntity rtsBe) {
            return createAndRegisterNewCustomBuilding(structureRL, structureName, level, pos.offset(1,0,1), rtsBe.getStructureSize());
        }
        return true;
    }

    // registers and places a new custom building on server and client
    public static boolean createAndRegisterNewCustomBuilding(ResourceLocation structureRL, String structureName, ServerLevel level,
                                                             BlockPos pos, Vec3i structureSize) {
        StructureTemplateManager manager = level.getStructureManager();
        Optional<StructureTemplate> template = manager.get(structureRL);
        CompoundTag structureNbt = null;
        if (template.isPresent()) {
            structureNbt = template.get().save(new CompoundTag());
        }
        if (structureNbt != null) {
            boolean buildingExists = false;
            for (BuildingPlacement existingPlacement : BuildingServerEvents.getBuildings()) {
                if (existingPlacement.originPos.equals(pos)) {
                    buildingExists = true;
                    break;
                }
            }
            if (!buildingExists) {
                ArrayList<BuildingBlock> blocks = BuildingUtils.getAbsoluteBlockData(
                        BuildingBlockData.getBuildingBlocksFromNbt(structureNbt),
                        level, pos, Rotation.NONE, new Vec3i(1,0,1)
                );
                int numSolidBlocks = 0;
                Block portraitBlock = Blocks.COMMAND_BLOCK;
                for (BuildingBlock bb : blocks) {
                    BlockState bs = bb.getBlockState();
                    if (!bs.isAir() && bs.getFluidState().isEmpty()) {
                        numSolidBlocks += 1;
                        portraitBlock = bs.getBlock();
                    }
                }
                if (numSolidBlocks == 0) {
                    PlayerServerEvents.sendMessageToAllPlayers("ERROR (server): cannot register custom building with no solid blocks");
                } else {
                    CustomBuilding building = new CustomBuilding(structureName, structureSize, portraitBlock, structureNbt);
                    for (CustomBuilding customBuilding : customBuildings) {
                        if (customBuilding.name.equals(building.name)) {
                            PlayerServerEvents.sendMessageToAllPlayers("ERROR (server): custom building " + building.name + " already exists");
                            return false;
                        }
                    }
                    customBuildings.add(building);
                    BuildingPlacement placement = new BuildingPlacement(building, level, pos, Rotation.NONE, "", blocks, false);
                    BuildingServerEvents.getBuildings().add(placement);
                    CustomBuildingClientboundPacket.registerCustomBuilding(building);
                    saveBuildings(level);
                    BuildingServerEvents.saveBuildings(level);
                    return true;
                }
            } else {
                PlayerServerEvents.sendMessageToAllPlayers("ERROR (server): cannot register custom building at same origin as another building");
            }
        }
        return false;
    }



    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent evt) {
        ServerLevel level = evt.getServer().getLevel(Level.OVERWORLD);
        if (level != null) {
            saveBuildings(level);
        }
    }

    public static void saveBuildings(ServerLevel level) {
        CustomBuildingSaveData customBuildingData = CustomBuildingSaveData.getInstance(level);
        customBuildingData.customBuildings.clear();
        customBuildings.forEach(b -> {
            customBuildingData.customBuildings.add(new CustomBuildingSave(
                    b.structureNbt,
                    b.name,
                    b.structureSize
            ));
        });
        customBuildingData.save();
        level.getDataStorage().save();
    }

    public static void loadBuildings(ServerLevel level) {
        CustomBuildingSaveData customBuildingData = CustomBuildingSaveData.getInstance(level);
        customBuildingData.customBuildings.forEach(b -> {
            CustomBuilding building = new CustomBuilding(b.buildingName, b.structureSize, Blocks.COMMAND_BLOCK, b.structureNbt);
            customBuildings.add(building);
            ReignOfNether.LOGGER.info("loaded custom building in serverevents: " + "" + "|" + b.buildingName);
        });
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent evt) {
        if (!PlayerServerEvents.rtsSyncingEnabled) {
            return;
        }
        MinecraftServer server = evt.getEntity().level().getServer();
        if (server == null || !server.isDedicatedServer()) {
            CompletableFuture.delayedExecutor(1000,  TimeUnit.MILLISECONDS).execute(() -> syncCustomBuildings(evt.getEntity().getName().getString()));
        } else {
            syncCustomBuildings(evt.getEntity().getName().getString());
        }
        //ReignOfNether.LOGGER.info("Synced " + buildings.size() + " custom buildings with player logged in");
    }

    private static void syncCustomBuildings(String playerName) {
        for (CustomBuilding customBuilding : customBuildings) {
            CustomBuildingClientboundPacket.registerCustomBuilding(playerName, customBuilding);
        }
    }
}
