package com.solegendary.reignofnether.hero;

import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HeroServerEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent evt) {
        Level level = evt.getEntity().getLevel();
        if (evt.getEntity() instanceof Unit unit) {
            HeroExperienceOrb expOrb = HeroExperienceOrb.newOrb(level,
                evt.getEntity().getX(),
                evt.getEntity().getY(),
                evt.getEntity().getZ(),
                unit.getCost().population
            );
            evt.getEntity().level.addFreshEntity(expOrb);
        }
    }
}
