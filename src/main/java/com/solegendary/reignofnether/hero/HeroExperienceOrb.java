package com.solegendary.reignofnether.hero;

import com.solegendary.reignofnether.registrars.EntityRegistrar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;

import java.util.Random;

public class HeroExperienceOrb extends ExperienceOrb {

    private static final Random RANDOM = new Random();

    public HeroExperienceOrb(EntityType<? extends ExperienceOrb> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static HeroExperienceOrb newOrb(Level pLevel, double pX, double pY, double pZ, int pValue) {
        HeroExperienceOrb expOrb = new HeroExperienceOrb(EntityRegistrar.HERO_EXPERIENCE_ORB.get(), pLevel);
        expOrb.setPos(pX, pY, pZ);
        expOrb.setYRot((float)(RANDOM.nextDouble() * 360.0));
        expOrb.setDeltaMovement(
            (RANDOM.nextDouble() * 0.2 - 0.1) * 2.0,
            RANDOM.nextDouble() * 0.2 * 2.0,
            (RANDOM.nextDouble() * 0.2 - 0.1) * 2.0
        );
        expOrb.value = pValue;
        return expOrb;
    }
}
