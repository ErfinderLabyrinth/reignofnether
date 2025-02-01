package com.solegendary.reignofnether.gamerules;

import com.solegendary.reignofnether.registrars.GameRuleRegistrar;
import com.solegendary.reignofnether.registrars.PacketHandler;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class GameruleServerboundPacket {

    GameruleAction action;
    String playerName;
    Long value;

    public static void syncMaxPopulation(long maxPopulation) {
        PacketHandler.INSTANCE.sendToServer(
            new GameruleServerboundPacket(GameruleAction.SET_MAX_POPULATION, "", maxPopulation));
    }

    public static void setOrthoviewMinY(long orthoviewMinY) {
        PacketHandler.INSTANCE.sendToServer(
            new GameruleServerboundPacket(GameruleAction.SET_GROUND_Y_LEVEL, "", orthoviewMinY));
    }

    public static void setImprovedPathfinding(boolean improvedPathfinding) {
        PacketHandler.INSTANCE.sendToServer(
            new GameruleServerboundPacket(GameruleAction.SET_IMPROVED_PATHFINDING, "", improvedPathfinding ? 1L : 0L));
    }

    public static void syncNeutralAggro(boolean neutralAggro) {
        PacketHandler.INSTANCE.sendToServer(
            new GameruleServerboundPacket(GameruleAction.SET_NEUTRAL_AGGRO, "", neutralAggro ? 1L : 0L));
    }

    public static void syncAllowBeacons(boolean allowBeacons) {
        PacketHandler.INSTANCE.sendToServer(
            new GameruleServerboundPacket(GameruleAction.SET_ALLOW_BEACONS, "", allowBeacons ? 1L : 0L));
    }

    public static void syncClassicAndBeaconModeOnly(boolean pvpModesOnly) {
        PacketHandler.INSTANCE.sendToServer(
            new GameruleServerboundPacket(GameruleAction.PVP_MODES_ONLY, "", pvpModesOnly ? 1L : 0L));
    }

    public GameruleServerboundPacket(GameruleAction action, String playerName, Long value) {
        this.action = action;
        this.playerName = playerName;
        this.value = value;
    }

    public GameruleServerboundPacket(FriendlyByteBuf buffer) {
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
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                success.set(false);
                return;
            }
            MinecraftServer server = player.getLevel().getServer();
            GameRules gameRules = player.getLevel().getGameRules();

            switch (action) {
                case SET_MAX_POPULATION -> {
                    UnitServerEvents.maxPopulation = Math.toIntExact(value);
                    gameRules.getRule(GameRuleRegistrar.MAX_POPULATION).set(UnitServerEvents.maxPopulation, server);
                    GameruleClientboundPacket.syncMaxPopulation(UnitServerEvents.maxPopulation);
                }
                case SET_GROUND_Y_LEVEL -> {
                    double groundYLevel = value;
                    gameRules.getRule(GameRuleRegistrar.GROUND_Y_LEVEL).set(UnitServerEvents.maxPopulation, server);
                    GameruleClientboundPacket.setOrthoviewMinY((long) groundYLevel + 30);
                }
                case SET_IMPROVED_PATHFINDING -> {
                    boolean booleanValue = value == 1L;
                    gameRules.getRule(GameRuleRegistrar.IMPROVED_PATHFINDING).set(booleanValue, server);
                    for (LivingEntity le : UnitServerEvents.getAllUnits()) {
                        UnitServerEvents.improvedPathfinding = booleanValue;
                        AttributeInstance ai = le.getAttribute(Attributes.FOLLOW_RANGE);
                        if (ai != null)
                            ai.setBaseValue(Unit.getFollowRange());
                    }
                    GameruleClientboundPacket.setImprovedPathfinding(booleanValue);
                }
                case SET_NEUTRAL_AGGRO -> {
                    boolean booleanValue = value == 1L;
                    gameRules.getRule(GameRuleRegistrar.NEUTRAL_AGGRO).set(booleanValue, server);
                    GameruleClientboundPacket.syncNeutralAggro(booleanValue);
                }
                case SET_ALLOW_BEACONS -> {
                    boolean booleanValue = value == 1L;
                    gameRules.getRule(GameRuleRegistrar.ALLOW_BEACONS).set(booleanValue, server);
                    GameruleClientboundPacket.syncAllowBeacons(booleanValue);
                }
                case PVP_MODES_ONLY -> {
                    boolean booleanValue = value == 1L;
                    gameRules.getRule(GameRuleRegistrar.PVP_MODES_ONLY).set(booleanValue, server);
                    GameruleClientboundPacket.syncClassicAndBeaconModeOnly(booleanValue);
                }
            }
            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}