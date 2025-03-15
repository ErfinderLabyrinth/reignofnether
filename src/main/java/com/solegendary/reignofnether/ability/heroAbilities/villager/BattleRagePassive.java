package com.solegendary.reignofnether.ability.heroAbilities.villager;

//The lower the guard's health, the more damage, resistance and life regen he gains
//Higher levels increase the amount of damage and resistance gained

// show vanilla villager angry clouds when the guard is hit

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;

public class BattleRagePassive extends HeroAbility {

    public BattleRagePassive(HeroUnit hero) {
        super(hero, 1, UnitAction.NONE, 0, 0, 0, false);
    }


}
