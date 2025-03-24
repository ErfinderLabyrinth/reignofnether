package com.solegendary.reignofnether.sandbox;

import com.solegendary.reignofnether.registrars.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class SandboxServerboundPacket {

    public SandboxAction sandboxAction;
    public String playerName;
    public String unitName;
    public BlockPos blockPos;
    public int entityId;

    public static void spawnUnit(SandboxAction sandboxAction, String playerName, String unitName, BlockPos blockPos) {
        if (!unitName.isBlank())
            PacketHandler.INSTANCE.sendToServer(new SandboxServerboundPacket(sandboxAction, playerName, unitName, blockPos, 0));
    }
    public static void setAnchor(BlockPos blockPos, int entityId) {
        PacketHandler.INSTANCE.sendToServer(new SandboxServerboundPacket(SandboxAction.SET_ANCHOR, "", "", blockPos, entityId));
    }
    public static void resetToAnchor(int entityId) {
        PacketHandler.INSTANCE.sendToServer(new SandboxServerboundPacket(SandboxAction.RESET_TO_ANCHOR, "", "", new BlockPos(0,0,0), entityId));
    }
    public static void removeAnchor(int entityId) {
        PacketHandler.INSTANCE.sendToServer(new SandboxServerboundPacket(SandboxAction.REMOVE_ANCHOR, "", "", new BlockPos(0,0,0), entityId));
    }

    public SandboxServerboundPacket(SandboxAction sandboxAction, String playerName, String unitName, BlockPos blockPos, int entityId) {
        this.sandboxAction = sandboxAction;
        this.playerName = playerName;
        this.unitName = unitName;
        this.blockPos = blockPos;
        this.entityId = entityId;
    }

    public SandboxServerboundPacket(FriendlyByteBuf buffer) {
        this.sandboxAction = buffer.readEnum(SandboxAction.class);
        this.playerName = buffer.readUtf();
        this.unitName = buffer.readUtf();
        this.blockPos = buffer.readBlockPos();
        this.entityId = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.sandboxAction);
        buffer.writeUtf(this.playerName);
        buffer.writeUtf(this.unitName);
        buffer.writeBlockPos(this.blockPos);
        buffer.writeInt(this.entityId);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            if (!SandboxServer.isAnyoneASandboxPlayer())
                return;

            switch (sandboxAction) {
                case SPAWN_UNIT -> SandboxServer.spawnUnit(this.playerName, this.unitName, this.blockPos);
                case SET_ANCHOR -> SandboxServer.setAnchor(this.entityId, this.blockPos);
                case RESET_TO_ANCHOR -> SandboxServer.resetToAnchor(this.entityId);
                case REMOVE_ANCHOR -> SandboxServer.removeAnchor(this.entityId);
            }
            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
