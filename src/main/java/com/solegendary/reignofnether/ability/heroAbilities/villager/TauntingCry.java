package com.solegendary.reignofnether.ability.heroAbilities.villager;

//Forces all nearby enemy units to lose control for a few seconds and target the royal guard
//While active, the guard gains knockback and push immunity
//Higher levels incease the taunt duration

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import net.minecraft.world.level.Level;

public class TauntingCry extends HeroAbility {

    public TauntingCry(HeroUnit hero) {
        super(hero, 3, UnitAction.TAUNTING_CRY, 30, 6, 0, false);
    }


}
