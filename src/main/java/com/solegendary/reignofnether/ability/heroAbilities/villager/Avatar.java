package com.solegendary.reignofnether.ability.heroAbilities.villager;

//Temporarily gain bonus damage, health and movespeed
//While active, the guard's model becomes larger and has a shimmering enchanted effect
// Duration is extended whenever damage is taken from an enemy

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class Avatar extends HeroAbility {

    public Avatar(HeroUnit hero) {
        super(hero, 3, UnitAction.AVATAR, 240, 0, 0, false);
    }


}
