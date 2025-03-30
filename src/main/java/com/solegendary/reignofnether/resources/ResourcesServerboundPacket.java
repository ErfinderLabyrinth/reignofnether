package com.solegendary.reignofnether.resources;

import com.solegendary.reignofnether.registrars.PacketHandler;
import com.solegendary.reignofnether.research.ResearchClientboundPacket;
import com.solegendary.reignofnether.research.ResearchServerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ResourcesServerboundPacket {

    public String playerGiving;
    public String playerReceiving;
    public ResourceName resourceName;
    public int amount;

    public static void sendResources(String playerGiving, String playerReceiving, ResourceName resourceName, int amount) {
        PacketHandler.INSTANCE.sendToServer(new ResourcesServerboundPacket(playerGiving, playerReceiving, resourceName, amount));
    }

    public ResourcesServerboundPacket(String playerGiving, String playerReceiving, ResourceName resourceName, int amount) {
        this.playerGiving = playerGiving;
        this.playerReceiving = playerReceiving;
        this.resourceName = resourceName;
        this.amount = amount;
    }

    public ResourcesServerboundPacket(FriendlyByteBuf buffer) {
        this.playerGiving = buffer.readUtf();
        this.playerReceiving = buffer.readUtf();
        this.resourceName = buffer.readEnum(ResourceName.class);
        this.amount = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.playerGiving);
        buffer.writeUtf(this.playerReceiving);
        buffer.writeEnum(this.resourceName);
        buffer.writeInt(this.amount);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            ResourcesServerEvents.trySendingResources(this.playerGiving, this.playerReceiving, this.resourceName, this.amount);
            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
