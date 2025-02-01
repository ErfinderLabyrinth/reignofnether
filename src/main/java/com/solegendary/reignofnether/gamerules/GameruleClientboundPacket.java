package com.solegendary.reignofnether.gamerules;

import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.gamemode.ClientGameModeHelper;
import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.solegendary.reignofnether.registrars.PacketHandler;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class GameruleClientboundPacket {

    GameruleAction action;
    String playerName;
    Long value;

    public static void syncMaxPopulation(long maxPopulation) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new GameruleClientboundPacket(GameruleAction.SET_MAX_POPULATION, "", maxPopulation));
    }

    public static void setOrthoviewMinY(long orthoviewMinY) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new GameruleClientboundPacket(GameruleAction.SET_GROUND_Y_LEVEL, "", orthoviewMinY));
    }

    public static void setImprovedPathfinding(boolean improvedPathfinding) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new GameruleClientboundPacket(GameruleAction.SET_IMPROVED_PATHFINDING, "", improvedPathfinding ? 1L : 0L));
    }

    public static void syncNeutralAggro(boolean neutralAggro) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new GameruleClientboundPacket(GameruleAction.SET_NEUTRAL_AGGRO, "", neutralAggro ? 1L : 0L));
    }

    public static void syncAllowBeacons(boolean allowBeacons) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new GameruleClientboundPacket(GameruleAction.SET_ALLOW_BEACONS, "", allowBeacons ? 1L : 0L));
    }

    public static void syncClassicAndBeaconModeOnly(boolean pvpModesOnly) {
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new GameruleClientboundPacket(GameruleAction.PVP_MODES_ONLY, "", pvpModesOnly ? 1L : 0L));
    }

    public GameruleClientboundPacket(GameruleAction action, String playerName, Long value) {
        this.action = action;
        this.playerName = playerName;
        this.value = value;
    }

    public GameruleClientboundPacket(FriendlyByteBuf buffer) {
        this.action = buffer.readEnum(GameruleAction.class);
        this.playerName = buffer.readUtf();
        this.value = buffer.readLong();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.action);
        buffer.writeUtf(this.playerName);
        buffer.writeLong(this.value);
    }

    // server-side packet-consuming functions
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> {
                        switch (action) {
                            case SET_MAX_POPULATION -> UnitClientEvents.setMaxPopulation(Math.toIntExact(value));
                            case SET_GROUND_Y_LEVEL -> OrthoviewClientEvents.setMinOrthoviewY(value);
                            case SET_IMPROVED_PATHFINDING -> UnitClientEvents.improvedPathfinding = value == 1L;
                            case SET_NEUTRAL_AGGRO -> UnitClientEvents.neutralAggro = value == 1L;
                            case SET_ALLOW_BEACONS -> BuildingClientEvents.allowBeacons = value == 1L;
                            case PVP_MODES_ONLY -> ClientGameModeHelper.setPvpModesOnly(value == 1L);
                        }
                        success.set(true);
                    });
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
