package com.solegendary.reignofnether.gamerules;

import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.solegendary.reignofnether.registrars.GameRuleRegistrar;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class GameruleServerEvents {

    @SubscribeEvent
    public static void onCommandUsed(CommandEvent evt) {
        List<ParsedCommandNode<CommandSourceStack>> nodes = evt.getParseResults().getContext().getNodes();
        if (nodes.size() <= 2)
            return;
        if (!nodes.get(0).getNode().getName().equals("gamerule"))
            return;

        if (nodes.get(1).getNode().getName().equals("maxPopulation")) {
            Map<String, ParsedArgument<CommandSourceStack, ?>> args = evt.getParseResults().getContext().getArguments();
            if (args.containsKey("value")) {
                UnitServerEvents.maxPopulation = (int) args.get("value").getResult();
                GameruleClientboundPacket.syncMaxPopulation(UnitServerEvents.maxPopulation);
            }
        } else if (nodes.get(1).getNode().getName().equals("groundYLevel")) {
            Map<String, ParsedArgument<CommandSourceStack, ?>> args = evt.getParseResults().getContext().getArguments();
            if (args.containsKey("value")) {
                double groundYLevel = ((Integer) args.get("value").getResult()).doubleValue();
                GameruleClientboundPacket.setOrthoviewMinY((long) groundYLevel + 30);
            }
        } else if (nodes.get(1).getNode().getName().equals("improvedPathfinding")) {
            Map<String, ParsedArgument<CommandSourceStack, ?>> args = evt.getParseResults().getContext().getArguments();
            if (args.containsKey("value")) {
                boolean value = (boolean) args.get("value").getResult();
                for (LivingEntity le : UnitServerEvents.getAllUnits()) {
                    UnitServerEvents.improvedPathfinding = value;
                    AttributeInstance ai = le.getAttribute(Attributes.FOLLOW_RANGE);
                    if (ai != null)
                        ai.setBaseValue(Unit.getFollowRange());
                }
                GameruleClientboundPacket.setImprovedPathfinding(value);
            }
        } else if (nodes.get(1).getNode().getName().equals("neutralAggro")) {
            Map<String, ParsedArgument<CommandSourceStack, ?>> args = evt.getParseResults().getContext().getArguments();
            if (args.containsKey("value")) {
                boolean value = (boolean) args.get("value").getResult();
                GameruleClientboundPacket.syncNeutralAggro(value);
            }
        } else if (nodes.get(1).getNode().getName().equals("allowBeacons")) {
            Map<String, ParsedArgument<CommandSourceStack, ?>> args = evt.getParseResults().getContext().getArguments();
            if (args.containsKey("value")) {
                boolean value = (boolean) args.get("value").getResult();
                GameruleClientboundPacket.syncAllowBeacons(value);
            }
        } else if (nodes.get(1).getNode().getName().equals("pvpModesOnly")) {
            Map<String, ParsedArgument<CommandSourceStack, ?>> args = evt.getParseResults().getContext().getArguments();
            if (args.containsKey("value")) {
                boolean value = (boolean) args.get("value").getResult();
                GameruleClientboundPacket.syncClassicAndBeaconModeOnly(value);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent evt) {
        MinecraftServer server = evt.getEntity().getLevel().getServer();
        if (server != null) {
            int maxPopulation = server.getGameRules().getInt(GameRuleRegistrar.MAX_POPULATION);
            GameruleClientboundPacket.syncMaxPopulation(maxPopulation);
            int groundYLevel = server.getGameRules().getRule(GameRuleRegistrar.GROUND_Y_LEVEL).get();
            GameruleClientboundPacket.setOrthoviewMinY(groundYLevel + 30);
            boolean neutralAggro = server.getGameRules().getRule(GameRuleRegistrar.NEUTRAL_AGGRO).get();
            GameruleClientboundPacket.syncNeutralAggro(neutralAggro);
            boolean allowBeacons = server.getGameRules().getRule(GameRuleRegistrar.ALLOW_BEACONS).get();
            GameruleClientboundPacket.syncAllowBeacons(allowBeacons);
            boolean pvpModesOnly = server.getGameRules().getRule(GameRuleRegistrar.PVP_MODES_ONLY).get();
            GameruleClientboundPacket.syncClassicAndBeaconModeOnly(pvpModesOnly);
        }
    }
}