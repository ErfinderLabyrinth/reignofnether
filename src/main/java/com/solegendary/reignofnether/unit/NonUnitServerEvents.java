package com.solegendary.reignofnether.unit;

import com.solegendary.reignofnether.research.ResearchServerEvents;
import com.solegendary.reignofnether.sandbox.SandboxServer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NonUnitServerEvents {

    // any mobs in this list will have their basic movement and attack goals cancelled until they become idle
    public static final List<PathfinderMob> controlledNonUnits = Collections.synchronizedList(new ArrayList<>());

    public static boolean canControlNonUnits(Level level, String playerName) {
        return SandboxServer.isSandboxPlayer(playerName) || ResearchServerEvents.playerHasCheat(playerName, "wouldyoukindly");
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END || evt.level.isClientSide() || evt.level.dimension() != Level.OVERWORLD) {
            return;
        }

        synchronized (controlledNonUnits) {
            controlledNonUnits.removeIf(mob -> (mob.isDeadOrDying() || mob.isRemoved() || (mob.getNavigation().isDone() && mob.getTarget() == null)));
        }
    }
}
