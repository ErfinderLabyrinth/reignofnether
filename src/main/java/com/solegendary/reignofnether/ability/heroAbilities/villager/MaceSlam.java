package com.solegendary.reignofnether.ability.heroAbilities.villager;

//Winds up and then smashes the ground in front of the guard, dealing area damage and stunning enemy units for a few seconds
//Higher levels incease the damage and stun duration

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class MaceSlam extends HeroAbility {

    public MaceSlam(HeroUnit hero) {
        super(hero, 3, UnitAction.MACE_SLAM, 20, 3, 0, false);
    }


}

