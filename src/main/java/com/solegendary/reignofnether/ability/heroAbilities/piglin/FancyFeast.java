package com.solegendary.reignofnether.ability.heroAbilities.piglin;

//Throws out a pile of food - friendly units automatically pick up this food and take a few seconds to eat it to instantly heal
//Higher levels raise the quality of food thrown
//Greed is Good raises the amount of food thrown

// TODO: make piglin units stop to eat vanilla food items if they are damaged

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;

public class FancyFeast extends HeroAbility {

    public FancyFeast(HeroUnit hero) {
        super(hero, 3, UnitAction.FANCY_FEAST, 45, 10, 0, false);
    }


}
