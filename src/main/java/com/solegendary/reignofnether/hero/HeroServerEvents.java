package com.solegendary.reignofnether.hero;

import com.solegendary.reignofnether.alliance.AlliancesServerEvents;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HeroServerEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent evt) {
        Level level = evt.getEntity().getLevel();
        if (evt.getEntity() instanceof Unit deadUnit) {
            for (LivingEntity unit : UnitServerEvents.getAllUnits()) {
                if (unit instanceof HeroUnit heroUnit) {

                    String heroOwner = ((Unit) heroUnit).getOwnerName();
                    String deadOwner = deadUnit.getOwnerName();

                    if (!AlliancesServerEvents.isAllied(heroOwner, deadOwner) && !heroOwner.equals(deadOwner) &&
                        heroUnit.getHeroLevel() < HeroUnit.MAX_HERO_LEVEL) {
                        HeroExperienceOrb expOrb = HeroExperienceOrb.newOrb(level,
                                heroUnit,
                                evt.getEntity().getX(),
                                evt.getEntity().getY(),
                                evt.getEntity().getZ(),
                                deadUnit.getCost().population
                        );
                        evt.getEntity().level.addFreshEntity(expOrb);
                    }
                }
            }
        }
    }
}
