package com.solegendary.reignofnether.research;

import com.solegendary.reignofnether.gamemode.GameModeClientboundPacket;
import com.solegendary.reignofnether.registrars.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ResearchServerboundPacket {

    public String playerName;
    public String itemName;
    public boolean add; // false for remove
    public boolean isCheat;
    public int value;

    public static void addCheat(String playerName, String itemName) {
        PacketHandler.INSTANCE.sendToServer(new ResearchServerboundPacket(playerName, itemName, true, true, 0));
    }
    public static void removeCheat(String playerName, String itemName) {
        PacketHandler.INSTANCE.sendToServer(new ResearchServerboundPacket(playerName, itemName, false, true, 0));
    }

    public ResearchServerboundPacket(String playerName, String itemName, boolean add, boolean isCheat, int value) {
        this.playerName = playerName;
        this.itemName = itemName;
        this.add = add;
        this.isCheat = isCheat;
        this.value = value;
    }

    public ResearchServerboundPacket(FriendlyByteBuf buffer) {
        this.playerName = buffer.readUtf();
        this.itemName = buffer.readUtf();
        this.add = buffer.readBoolean();
        this.isCheat = buffer.readBoolean();
        this.value = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.playerName);
        buffer.writeUtf(this.itemName);
        buffer.writeBoolean(this.add);
        buffer.writeBoolean(this.isCheat);
        buffer.writeInt(this.value);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            if (isCheat) {
                if (add) {
                    ResearchServerEvents.addCheat(this.playerName, this.itemName);
                    ResearchClientboundPacket.addCheat(this.playerName, this.itemName);
                }
                else {
                    ResearchServerEvents.removeCheat(this.playerName, this.itemName);
                    ResearchClientboundPacket.removeCheat(this.playerName, this.itemName);
                }
            }
            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
