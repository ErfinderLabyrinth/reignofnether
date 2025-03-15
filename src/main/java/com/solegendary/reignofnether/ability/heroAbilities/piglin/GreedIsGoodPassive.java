package com.solegendary.reignofnether.ability.heroAbilities.piglin;

//Causes other abilities to use your resources to gain improved effects
//Higher levels raise the effects but also cost of the buff
//This can be toggled this on and off

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class GreedIsGoodPassive extends HeroAbility {

    public GreedIsGoodPassive(HeroUnit hero) {
        super(hero, 1, UnitAction.TOGGLE_GREED_IS_GOOD_PASSIVE, 0, 0, 0, false);
    }



}
