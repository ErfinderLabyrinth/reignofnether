package com.solegendary.reignofnether.building.custombuilding;

import com.solegendary.reignofnether.registrars.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CustomBuildingClientboundPacket {

    // pos is used to identify the building object serverside
    public String playerName;
    public String name;
    public BlockPos structureSize;
    public CompoundTag structureNbt;

    public static void registerCustomBuilding(CustomBuilding building) {
        registerCustomBuilding("", building);
    }

    public static void registerCustomBuilding(String playerName, CustomBuilding building) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new CustomBuildingClientboundPacket(
                playerName, building.name, new BlockPos(building.structureSize), building.structureNbt
        ));
    }

    public CustomBuildingClientboundPacket(
            String playerName,
            String name,
            BlockPos structureSize,
            CompoundTag structureNbt
    ) {
        this.playerName = playerName;
        this.name = name;
        this.structureSize = structureSize;
        this.structureNbt = structureNbt;
    }

    public CustomBuildingClientboundPacket(FriendlyByteBuf buffer) {
        this.playerName = buffer.readUtf();
        this.name = buffer.readUtf();
        this.structureSize = buffer.readBlockPos();
        this.structureNbt = buffer.readNbt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.playerName);
        buffer.writeUtf(this.name);
        buffer.writeBlockPos(this.structureSize);
        buffer.writeNbt(this.structureNbt);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                CustomBuildingClientEvents.registerCustomBuilding(playerName, name, structureSize, structureNbt);
                success.set(true);
            });
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
