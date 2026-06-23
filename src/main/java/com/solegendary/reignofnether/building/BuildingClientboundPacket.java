package com.solegendary.reignofnether.building;

import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.buildings.placements.PortalPlacement;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.custombuilding.CustomBuilding;
import com.solegendary.reignofnether.building.custombuilding.CustomBuildingClientEvents;
import com.solegendary.reignofnether.building.production.ActiveProduction;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.registrars.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static com.solegendary.reignofnether.building.BuildingUtils.findBuilding;

public abstract class BuildingClientboundPacket {
    public static final ResourceLocation EMPTY = ResourceLocation.fromNamespaceAndPath("", "");

    // pos is used to identify the building object serverside
    public BuildingAction action;
    public BlockPos buildingPos;

    @Deprecated(forRemoval = true)
    public static void placeBuilding(
        BlockPos buildingPos,
        Building building,
        Rotation rotation,
        String ownerName,
        int scenarioRoleIndex,
        int numQueuedBlocks,
        boolean isDiagonalBridge,
        int upgradeLevel,
        boolean isBuilt,
        PortalPlacement.PortalType portalType,
        BlockPos portalDestination,
        boolean forPlayerLoggingIn
    ) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new PlaceBuilding(
            building instanceof CustomBuilding ? BuildingAction.PLACE_CUSTOM : BuildingAction.PLACE,
            building instanceof CustomBuilding ? ResourceLocation.fromNamespaceAndPath("rts-cb", building.name) : ReignOfNetherRegistries.BUILDING.getKey(building),
            buildingPos,
            rotation,
            ownerName,
            scenarioRoleIndex,
            0,
            numQueuedBlocks,
            isDiagonalBridge,
            upgradeLevel,
            isBuilt,
            portalType,
            portalDestination,
            forPlayerLoggingIn
        ));
    }

    public static class PlaceBuilding extends BuildingClientboundPacket {
        ResourceLocation buildingKey;
        Rotation rotation;
        String ownerName;
        int scenarioRoleIndex;
        int blocksPlaced; // for syncing out-of-view clientside buildings
        int numQueuedBlocks; // used for delaying destroy checks clientside
        boolean isDiagonalBridge;
        int upgradeLevel;
        boolean isBuilt;
        PortalPlacement.PortalType portalType;
        BlockPos portalDestination;
        boolean forPlayerLoggingIn; // is this placement for someone logging in currently joined?

        public PlaceBuilding(BuildingAction action, ResourceLocation buildingKey, BlockPos buildingPos, Rotation rotation, String ownerName, int scenarioRoleIndex, int blocksPlaced, int numQueuedBlocks, boolean isDiagonalBridge, int upgradeLevel, boolean isBuilt, PortalPlacement.PortalType portalType, BlockPos portalDestination, boolean forPlayerLoggingIn) {
            super(action, buildingPos);
            this.buildingKey = buildingKey;
            this.rotation = rotation;
            this.ownerName = ownerName;
            this.scenarioRoleIndex = scenarioRoleIndex;
            this.blocksPlaced = blocksPlaced;
            this.numQueuedBlocks = numQueuedBlocks;
            this.isDiagonalBridge = isDiagonalBridge;
            this.upgradeLevel = upgradeLevel;
            this.isBuilt = isBuilt;
            this.portalType = portalType;
            this.portalDestination = portalDestination;
            this.forPlayerLoggingIn = forPlayerLoggingIn;
        }

        public PlaceBuilding(FriendlyByteBuf buffer) {
            super(buffer);

            this.buildingKey = buffer.readResourceLocation();
            this.rotation = buffer.readEnum(Rotation.class);
            this.ownerName = buffer.readUtf();
            this.scenarioRoleIndex = buffer.readInt();
            this.blocksPlaced = buffer.readInt();
            this.numQueuedBlocks = buffer.readInt();
            this.isDiagonalBridge = buffer.readBoolean();
            this.upgradeLevel = buffer.readInt();
            this.isBuilt = buffer.readBoolean();
            this.portalType = buffer.readEnum(PortalPlacement.PortalType.class);
            this.portalDestination = buffer.readBlockPos();
            this.forPlayerLoggingIn = buffer.readBoolean();
        }

        @Override
        public void encode(FriendlyByteBuf buffer) {
            super.encode(buffer);
            buffer.writeResourceLocation(buildingKey);
            buffer.writeEnum(rotation);
            buffer.writeUtf(ownerName);
            buffer.writeInt(scenarioRoleIndex);
            buffer.writeInt(blocksPlaced);
            buffer.writeInt(numQueuedBlocks);
            buffer.writeBoolean(isDiagonalBridge);
            buffer.writeInt(upgradeLevel);
            buffer.writeBoolean(isBuilt);
            buffer.writeEnum(portalType);
            buffer.writeBlockPos(portalDestination);
            buffer.writeBoolean(forPlayerLoggingIn);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            BuildingClientEvents.placeBuilding(
                    action == BuildingAction.PLACE_CUSTOM
                            ? CustomBuildingClientEvents.getCustomBuilding(this.buildingKey.getPath())
                            : ReignOfNetherRegistries.BUILDING.get(this.buildingKey),
                    this.buildingPos,
                    this.rotation,
                    this.ownerName,
                    this.numQueuedBlocks,
                    this.isDiagonalBridge,
                    this.upgradeLevel,
                    this.isBuilt,
                    this.portalType,
                    this.portalDestination,
                    this.forPlayerLoggingIn
            );
        }
    }

    @Deprecated(forRemoval = true)
    public static void syncBuilding(BlockPos buildingPos, int blocksPlaced, double partialBlocksDestroyed, String ownerName, int scenarioRoleIndex) {
        BuildingClientboundPacket packet = new SyncBuilding(
                buildingPos,
                ownerName,
                blocksPlaced,
                scenarioRoleIndex,
                partialBlocksDestroyed
        );
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static class SyncBuilding extends BuildingClientboundPacket {
        String ownerName;
        int blocksPlaced; // for syncing out-of-view clientside buildings
        int scenarioRoleIndex;
        double partialBlocksDestroyed;
        public SyncBuilding(BlockPos buildingPos, String ownerName, int blocksPlaced, int scenarioRoleIndex, double partialBlocksDestroyed) {
            super(BuildingAction.SYNC_BLOCKS_AND_OWNER, buildingPos);
            this.ownerName = ownerName;
            this.blocksPlaced = blocksPlaced;
            this.scenarioRoleIndex = scenarioRoleIndex;
            this.partialBlocksDestroyed = partialBlocksDestroyed;
        }

        public SyncBuilding(FriendlyByteBuf buffer) {
            super(buffer);
            ownerName = buffer.readUtf();
            blocksPlaced = buffer.readInt();
            scenarioRoleIndex = buffer.readInt();
            partialBlocksDestroyed = buffer.readDouble();
        }

        @Override
        public void encode(FriendlyByteBuf buffer) {
            super.encode(buffer);
            buffer.writeUtf(ownerName);
            buffer.writeInt(blocksPlaced);
            buffer.writeInt(scenarioRoleIndex);
            buffer.writeDouble(partialBlocksDestroyed);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            BuildingClientEvents.syncBuilding(building, this.blocksPlaced, this.partialBlocksDestroyed, this.ownerName, this.scenarioRoleIndex);
        }
    }

    @Deprecated(forRemoval = true)
    public static void startProduction(BlockPos buildingPos, ProductionItem item) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
            new StartProduction(ReignOfNetherRegistries.PRODUCTION_ITEM.getKey(item),
                buildingPos
            )
        );
    }

    public static class StartProduction extends BuildingClientboundPacket {
        ResourceLocation productionKey;
        public StartProduction(ResourceLocation productionKey, BlockPos buildingPos) {
            super(BuildingAction.START_PRODUCTION, buildingPos);
            this.productionKey = productionKey;
        }

        public StartProduction(FriendlyByteBuf buffer) {
            super(buffer);
            productionKey = buffer.readResourceLocation();
        }

        @Override
        public void encode(FriendlyByteBuf buffer) {
            super.encode(buffer);
            buffer.writeResourceLocation(productionKey);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            ((ProductionPlacement) building).startProductionItem(
                ReignOfNetherRegistries.PRODUCTION_ITEM.get(productionKey)
            );
        }
    }

    @Deprecated(forRemoval = true)
    public static void cancelProduction(BlockPos buildingPos, ProductionItem item, boolean frontItem) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
            new CancelProduction(frontItem
                                          ? BuildingAction.CANCEL_PRODUCTION
                                          : BuildingAction.CANCEL_BACK_PRODUCTION,
                ReignOfNetherRegistries.PRODUCTION_ITEM.getKey(item),
                buildingPos
            )
        );
    }

    public static class CancelProduction extends BuildingClientboundPacket {  // Maybe combine with StartProduction
        ResourceLocation productionKey;
        public CancelProduction(BuildingAction action, ResourceLocation productionKey, BlockPos buildingPos) {
            super(action, buildingPos);
            this.productionKey = productionKey;
        }

        public CancelProduction(FriendlyByteBuf buffer) {
            super(buffer);
            productionKey = buffer.readResourceLocation();
        }

        @Override
        public void encode(FriendlyByteBuf buffer) {
            super.encode(buffer);
            buffer.writeResourceLocation(productionKey);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            ((ProductionPlacement) building).cancelProductionItem(
                    ReignOfNetherRegistries.PRODUCTION_ITEM.get(productionKey),
                action == BuildingAction.CANCEL_PRODUCTION
            );
        }
    }

    @Deprecated(forRemoval = true)
    public static void changePortal(BlockPos buildingPos, PortalPlacement.PortalType type) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
            new ChangePortal(
                buildingPos,
                type
            )
        );
    }

    public static class ChangePortal extends BuildingClientboundPacket {
        PortalPlacement.PortalType portalType;
        public ChangePortal(BlockPos buildingPos, PortalPlacement.PortalType portalType) {
            super(BuildingAction.CHANGE_PORTAL, buildingPos);
            this.portalType = portalType;
        }

        public ChangePortal(FriendlyByteBuf buffer) {
            super(buffer);
            portalType = buffer.readEnum(PortalPlacement.PortalType.class);
        }

        @Override
        public void encode(FriendlyByteBuf buffer) {
            super.encode(buffer);
            buffer.writeEnum(portalType);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            if (building instanceof PortalPlacement portal) {
                portal.changePortalStructure(portalType);
            }
        }
    }

    @Deprecated(forRemoval = true)
    public static void clearQueue(BlockPos buildingPos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
            new ClearQueue(buildingPos)
        );
    }

    public static class ClearQueue extends BuildingClientboundPacket {
        public ClearQueue(BlockPos buildingPos) {
            super(BuildingAction.CLEAR_PRODUCTION, buildingPos);
        }

        public ClearQueue(FriendlyByteBuf buffer) {
            super(buffer);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            if (building instanceof ProductionPlacement pBuilding) {
                if (!pBuilding.productionQueue.isEmpty()) {
                    ActiveProduction pItem = pBuilding.productionQueue.get(0);
                    if (!pItem.completed) {
                        pItem.completed = true;
                        pItem.item.onComplete.accept(pBuilding.level, pBuilding);
                    }
                    pBuilding.productionQueue.clear();
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public static void completeProduction(BlockPos buildingPos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
            new CompleteProduction(buildingPos)
        );
    }

    public static class CompleteProduction extends BuildingClientboundPacket {
        public CompleteProduction(BlockPos buildingPos) {
            super(BuildingAction.COMPLETE_PRODUCTION, buildingPos);
        }

        public CompleteProduction(FriendlyByteBuf buffer) {
            super(buffer);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            if (building instanceof ProductionPlacement pBuilding) {
                if (!pBuilding.productionQueue.isEmpty()) {
                    ActiveProduction pItem = pBuilding.productionQueue.get(0);
                    if (!pItem.completed) {
                        pItem.completed = true;
                        pItem.item.onComplete.accept(pBuilding.level, pBuilding);
                    }
                    pBuilding.productionQueue.remove(pItem);
                }
            }
        }
    }

    @Deprecated(forRemoval = true)
    public static void removeBuilding(BlockPos buildingPos) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
            new RemoveBuilding(buildingPos)
        );
    }

    public static class RemoveBuilding extends BuildingClientboundPacket {
        public RemoveBuilding(BlockPos buildingPos) {
            super(BuildingAction.REMOVE, buildingPos);
        }

        public RemoveBuilding(FriendlyByteBuf buffer) {
            super(buffer);
        }

        @Override
        protected void handle(@Nullable BuildingPlacement building) {
            BuildingClientEvents.removeBuilding(buildingPos);
        }
    }

    public BuildingClientboundPacket(
            BuildingAction action,
            BlockPos buildingPos
    ) {
        this.action = action;
        this.buildingPos = buildingPos;
    }

    public BuildingClientboundPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(BuildingAction.class);
        this.buildingPos = buffer.readBlockPos();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeBlockPos(this.buildingPos);
    }

    public void broadcast() {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), this);
    }

    public void sendTo(ServerPlayer player) {
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), this);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                BuildingPlacement building = null;
                if (this.action != BuildingAction.PLACE &&
                    this.action != BuildingAction.PLACE_CUSTOM) {
                    building = findBuilding(true, this.buildingPos);
                    if (building == null) {

                        // if the client was missing a building, replace it
                        if (this.action == BuildingAction.SYNC_BLOCKS_AND_OWNER) {
                            BuildingServerboundPacket.requestReplacement(this.buildingPos);
                            //ReignOfNether.LOGGER.warn("Missing building");
                        }
                        return;
                    }
                }

                handle(building);

                success.set(true);
            });
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

    protected abstract void handle(@Nullable BuildingPlacement building);
}
